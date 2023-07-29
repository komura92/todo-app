package com.example.todoapp.domain.constants;

import com.example.todoapp.domain.model.TaskDto;
import com.example.todoapp.domain.model.TaskStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskTestConstants {
    public static final TaskDto CREATE_TASK_DTO = TaskDto.builder()
            .name("create task name")
            .description("create task description")
            .priority(8)
            .deadline(LocalDateTime.of(2023, 9, 12, 15,30))
            .build();

    public static final TaskDto USER_2_TASK_UPDATE = TaskDto.builder()
            .id(16L)
            .name("update task name")
            .description("update task description")
            .priority(7)
            .deadline(LocalDateTime.of(2023, 9, 12, 16,30))
            .build();

    public static final TaskDto CREATE_TASK_WITH_PREDEFINED_DTO = TaskDto.builder()
            .name("create task name")
            .description("create task description")
            .priority(8)
            .deadline(LocalDateTime.of(2023, 9, 12, 15,30))
            .status(TaskStatus.DONE)
            .build();

    public static final TaskDto UPDATE_TASK_DTO = TaskDto.builder()
            .id(2L)
            .name("update task name")
            .description("update task description")
            .priority(7)
            .deadline(LocalDateTime.of(2024, 9, 12, 16,30))
            .build();
}
