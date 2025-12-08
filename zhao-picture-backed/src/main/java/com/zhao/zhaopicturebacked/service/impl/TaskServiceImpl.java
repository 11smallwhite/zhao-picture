package com.zhao.zhaopicturebacked.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.zhaopicturebacked.domain.Task;
import com.zhao.zhaopicturebacked.service.TaskService;
import com.zhao.zhaopicturebacked.mapper.TaskMapper;
import org.springframework.stereotype.Service;

/**
* @author Vip
* @description 针对表【task(任务表)】的数据库操作Service实现
* @createDate 2025-12-04 12:37:58
*/
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task>
    implements TaskService{

}




