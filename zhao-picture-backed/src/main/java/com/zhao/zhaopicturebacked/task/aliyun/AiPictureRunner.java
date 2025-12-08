package com.zhao.zhaopicturebacked.task.aliyun;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

// 添加日志注解，输出关键信息
@Slf4j
// 建议实现 Runnable（而非继承 Thread），符合面向接口编程
public class AiPictureRunner implements Runnable {

    private final String taskId;
    private final Long userId;
    // 全局唯一的 Scheduler 实例（避免多次创建）
    private static volatile Scheduler GLOBAL_SCHEDULER;

    static {
        // 静态初始化：确保全局只有一个 Scheduler
        try {
            GLOBAL_SCHEDULER = StdSchedulerFactory.getDefaultScheduler();
            if (!GLOBAL_SCHEDULER.isStarted()) {
                GLOBAL_SCHEDULER.start(); // 全局启动一次即可
                log.info("Quartz 全局调度器初始化并启动成功");
            }
        } catch (SchedulerException e) {
            log.error("Quartz 全局调度器初始化失败", e);
            throw new RuntimeException("调度器初始化失败", e);
        }
    }

    public AiPictureRunner(String taskId, Long userId) {
        this.taskId = taskId;
        this.userId = userId;
    }

    @Override
    public void run() {
        try {
            log.info("开始创建AI扩图定时任务，taskId={}", taskId);

            // 1. 定义任务唯一标识（避免重复创建）
            String jobName = "AiPictureJob-" + taskId;
            String jobGroup = "AiPictureGroup";
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);

            // 检查任务是否已存在，避免重复创建
            if (GLOBAL_SCHEDULER.checkExists(jobKey)) {
                log.warn("AI扩图定时任务已存在，taskId={}", taskId);
                return;
            }

            // 2. 创建 Trigger（触发器）：每2秒执行一次，简化 Cron 表达式
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .usingJobData("taskId", taskId)
                    .usingJobData("userId", userId)
                    .usingJobData("maxExecCount", 5) // 最大执行5次
                    // 简化 Cron：每2秒执行（等价于 0,2,4...58 * * * * ?）
                    .withSchedule(CronScheduleBuilder.cronSchedule("*/4 * * * * ?"))
                    .build();

            // 3. 创建 JobDetail（任务）
            JobDetail job = JobBuilder.newJob(AiPictureJob.class)
                    .withIdentity(jobKey)
                    .build();

            // 4. 提交任务到全局调度器
            GLOBAL_SCHEDULER.scheduleJob(job, trigger);
            log.info("AI扩图定时任务创建成功，taskId={}, jobKey={}", taskId, jobKey);

        } catch (Exception e) {
            log.error("创建AI扩图定时任务失败，taskId={}", taskId, e);
            throw new RuntimeException("创建定时任务失败", e);
        }
    }
}