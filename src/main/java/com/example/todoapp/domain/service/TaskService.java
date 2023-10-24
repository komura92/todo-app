package com.example.todoapp.domain.service;

import com.example.todoapp.domain.entity.Task;
import com.example.todoapp.domain.exception.TaskNotFoundException;
import com.example.todoapp.domain.mapper.TaskMapper;
import com.example.todoapp.domain.model.TaskDto;
import com.example.todoapp.domain.model.TaskStatus;
import com.example.todoapp.domain.repository.TaskRepository;
import com.example.todoapp.infrastructure.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public TaskDto createUpdateTask(TaskDto taskDto) {
        return Optional.ofNullable(taskDto.id())
                .map(taskId -> updateTask(taskDto))
                .orElseGet(() -> createTask(taskDto));
    }

    private TaskDto createTask(TaskDto taskDto) {
        Task newTask = taskRepository.save(TaskMapper.toCreateEntity(taskDto, UserUtils.getActualUserId()));
        return TaskMapper.toDto(newTask);
    }

    private TaskDto updateTask(TaskDto taskDto) {
        Task task = taskRepository.getMyTask(taskDto.id(), UserUtils.getActualUserId(), TaskStatus.OPEN.name())
                .orElseThrow(TaskNotFoundException::new);
        TaskMapper.updateEntity(task, taskDto);
        return TaskMapper.toDto(taskRepository.save(task));
    }

    public void deleteTask(Long taskId) {
        Task task = taskRepository.getMyTask(taskId, UserUtils.getActualUserId(), TaskStatus.OPEN.name())
                .orElseThrow(TaskNotFoundException::new);
        taskRepository.delete(task);
    }

    public void markTaskAsDone(Long taskId) {
        Task task = taskRepository.getMyTask(taskId, UserUtils.getActualUserId(), TaskStatus.OPEN.name())
                .orElseThrow(TaskNotFoundException::new);
        TaskMapper.markAsDone(task);
        taskRepository.save(task);
    }

    public List<TaskDto> getMyOpenTasks(Long startIndex, int quantity) {
        List<Task> myTasks = taskRepository.getMyTasksByStatus(UserUtils.getActualUserId(), TaskStatus.OPEN.name(), startIndex, quantity);
        return TaskMapper.toDtos(myTasks);
    }

    public List<TaskDto> getMyDoneTasks(Long startIndex, int quantity) {
        List<Task> myTasks = taskRepository.getMyTasksByStatus(UserUtils.getActualUserId(), TaskStatus.DONE.name(), startIndex, quantity);
        return TaskMapper.toDtos(myTasks);
    }
}
