package com.zhao.zhaopicturebacked.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 任务表
 * @TableName task
 */
@TableName(value ="task")
@Data
public class Task implements Serializable {
    /**
     * 任务id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务唯一标识
     */
    @TableField(value = "task_id")
    private String taskId;

    /**
     * 任务状态：PENDING-排队中，RUNNING-处理中，SUSPENDED-挂起，SUCCEEDED-执行成功，FAILED-执行失败，UNKNOWN-任务不存在或状态未知
     */
    @TableField(value = "task_status")
    private String taskStatus;

    /**
     * 提交时间（格式：YYYY-MM-DD HH:mm:ss.SSS）
     */
    @TableField(value = "submit_time")
    private Date submitTime;

    /**
     * 调度时间（格式：YYYY-MM-DD HH:mm:ss.SSS）
     */
    @TableField(value = "scheduled_time")
    private Date scheduledTime;

    /**
     * 结束时间（格式：YYYY-MM-DD HH:mm:ss.SSS）
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 输出图像的URL
     */
    @TableField(value = "output_image_url")
    private String outputImageUrl;

    /**
     * 接口错误码（接口成功请求不会返回该参数）
     */
    @TableField(value = "code")
    private String code;

    /**
     * 接口错误信息（接口成功请求不会返回该参数）
     */
    @TableField(value = "message")
    private String message;

    /**
     * 任务指标信息（JSON格式存储）
     */
    @TableField(value = "task_metrics")
    private Object taskMetrics;

    /**
     * 创建用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 编辑时间
     */
    @TableField(value = "edit_time")
    private Date editTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除（0-未删除，1-已删除）
     */
    @TableLogic
    @TableField(value = "is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}