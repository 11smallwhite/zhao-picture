-- auto-generated definition
create table task
(
    id               bigint auto_increment comment '任务id'
        primary key,
    task_id          varchar(64)                        not null comment '任务唯一标识',
    task_status      varchar(32)                        not null comment '任务状态：PENDING-排队中，RUNNING-处理中，SUSPENDED-挂起，SUCCEEDED-执行成功，FAILED-执行失败，UNKNOWN-任务不存在或状态未知',
    submit_time      datetime                           null comment '提交时间（格式：YYYY-MM-DD HH:mm:ss.SSS）',
    scheduled_time   datetime                           null comment '调度时间（格式：YYYY-MM-DD HH:mm:ss.SSS）',
    end_time         datetime                           null comment '结束时间（格式：YYYY-MM-DD HH:mm:ss.SSS）',
    output_image_url varchar(512)                       null comment '输出图像的URL',
    code             varchar(32)                        null comment '接口错误码（接口成功请求不会返回该参数）',
    message          varchar(512)                       null comment '接口错误信息（接口成功请求不会返回该参数）',
    task_metrics     json                               null comment '任务指标信息（JSON格式存储）',
    user_id          bigint                             not null comment '创建用户id',
    create_time      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    edit_time        datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete        tinyint  default 0                 not null comment '是否删除（0-未删除，1-已删除）',
    constraint uk_task_id
        unique (task_id)
)
    comment '任务表' collate = utf8mb4_unicode_ci;

create index idx_submit_time
    on task (submit_time);

create index idx_task_status
    on task (task_status);

create index idx_update_time
    on task (update_time);

create index idx_user_id
    on task (user_id);

