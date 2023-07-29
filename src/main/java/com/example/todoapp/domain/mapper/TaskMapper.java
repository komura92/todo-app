package com.example.todoapp.domain.mapper;

import com.example.todoapp.domain.entity.Task;
import com.example.todoapp.domain.model.TaskDto;
import com.example.todoapp.domain.model.TaskStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskMapper {
    public static void updateEntity(Task task, TaskDto taskDto) {
        task.setDeadline(taskDto.getDeadline());
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setPriority(taskDto.getPriority());
    }

    public static Task toCreateEntity(TaskDto taskDto, String userId) {
        return Task.builder()
                .name(taskDto.getName())
                .description(taskDto.getDescription())
                .deadline(taskDto.getDeadline())
                .userId(userId)
                .status(TaskStatus.OPEN.name())
                .priority(taskDto.getPriority())
                .build();
    }

    public static TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .status(TaskStatus.valueOf(task.getStatus()))
                .priority(task.getPriority())
                .build();
    }

    public static void markAsDone(Task task) {
        task.setStatus(TaskStatus.DONE.name());
    }

    public static List<TaskDto> toDtos(List<Task> tasks) {
        return tasks.stream()
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }
}
