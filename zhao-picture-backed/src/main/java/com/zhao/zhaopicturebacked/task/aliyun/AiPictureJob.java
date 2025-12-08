package com.zhao.zhaopicturebacked.task.aliyun;


import com.zhao.zhaopicturebacked.api.aliyun.AliYunApi;
import com.zhao.zhaopicturebacked.api.aliyun.GetOutPaintingTaskResponse;
import com.zhao.zhaopicturebacked.domain.Task;
import com.zhao.zhaopicturebacked.enums.CodeEnum;
import com.zhao.zhaopicturebacked.service.TaskService;
import com.zhao.zhaopicturebacked.task.SpringContextHolder;
import com.zhao.zhaopicturebacked.utils.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.BeanUtils;

import java.util.Date;


// 关键1：持久化JobDataMap的修改（执行后保存map）
@PersistJobDataAfterExecution
// 关键2：禁止并发执行（避免同一任务多次执行导致计数混乱）
@DisallowConcurrentExecution
@Slf4j
public class AiPictureJob implements Job {



    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("现在的时间是：{}",new Date());
        log.info("执行Ai扩图的定时任务");
        try {
            JobKey jobKey = jobExecutionContext.getJobDetail().getKey(); // 当前任务的唯一标识
            TriggerKey triggerKey = jobExecutionContext.getTrigger().getKey(); // 当前触发器的唯一标识
            Scheduler scheduler = jobExecutionContext.getScheduler();
            //获取taskId
            JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
            String taskId = jobDataMap.getString("taskId");
            if (taskId == null) {
                log.error("任务ID为空，终止执行");
                return;
            }

            //最大执行次数
            Integer maxExecCount = (Integer) jobDataMap.get("maxExecCount");
            maxExecCount = (maxExecCount == null || maxExecCount <= 0) ? 5 : maxExecCount;
            // 获取当前执行次数（初始为0，每次执行+1）

            Integer execCount = (Integer) jobDataMap.get("execCount");
            execCount = (execCount == null || execCount <= 0) ? 1 : execCount + 1;
            //判断执行次数是否达到上限
            log.info("任务{}已执行{}次", taskId, execCount);
            if (execCount >= maxExecCount) {
                log.info("任务{}执行次数达到上限{}，强制停止定时任务", taskId, maxExecCount);
                // 执行删除任务逻辑
                // 4. 从调度器中删除当前任务（核心：停止后续执行）

                // 先暂停触发器 → 移除触发器 → 删除任务（Quartz标准删除流程）
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                scheduler.deleteJob(jobKey);

                log.info("任务{}的定时任务已成功删除，后续不再执行", taskId);
            }
            // 3. 持久化更新后的execCount（核心：写回Trigger的JobDataMap）
            Trigger oldTrigger = scheduler.getTrigger(triggerKey);
            JobDataMap newTriggerData = oldTrigger.getJobDataMap();
            newTriggerData.put("execCount", execCount); // 更新执行次数
            // 重新构建Trigger（保留原有调度规则，仅更新JobDataMap）
            Trigger newTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(oldTrigger.getScheduleBuilder())
                    .usingJobData(newTriggerData)
                    .forJob(jobKey)
                    .build();
            // 重新调度Trigger，使修改生效
            scheduler.rescheduleJob(triggerKey, newTrigger);


            //调用阿里云AI的查询任务接口
            AliYunApi aliYunApi = SpringContextHolder.getBean(AliYunApi.class);
            TaskService taskService = SpringContextHolder.getBean(TaskService.class);

            GetOutPaintingTaskResponse aiTask = aliYunApi.getAiTask(taskId);

            String taskStatus = aiTask.getOutput().getTaskStatus();
            //如果任务调用成功，则保存任务信息到数据库，并暂停删除定时任务
            if (taskStatus.equals("SUCCEEDED")) {
                Task task = new Task();
                BeanUtils.copyProperties(aiTask.getOutput(), task);
                Long userId = (Long)jobDataMap.get("userId");
                task.setUserId(userId);
                taskService.lambdaUpdate().eq(Task::getTaskId, taskId).update(task);

                // 先暂停触发器 → 移除触发器 → 删除任务（Quartz标准删除流程）
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                scheduler.deleteJob(jobKey);

                log.info("任务{}的定时任务已成功删除，后续不再执行", taskId);
            }


        } catch (JobExecutionException e) {
            // Quartz允许的异常，直接抛出（可控制重试）
            log.error("任务执行异常");
            throw e;
        } catch (Exception e) {
            // 捕获所有其他异常，转为JobExecutionException（核心修复：移除自定义异常）
            log.error("任务{}执行异常（非预期异常），jobKey={}", e);
            // 第二个参数：true=重试，false=不重试（根据业务调整）
            throw new JobExecutionException("任务执行异常：" + e.getMessage(), e, false);
        }
    }
}
