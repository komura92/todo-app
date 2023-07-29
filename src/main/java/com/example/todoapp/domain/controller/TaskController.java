package com.example.todoapp.domain.controller;

import com.example.todoapp.domain.model.TaskDto;
import com.example.todoapp.domain.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(TaskController.PATH)
public class TaskController {
    public static final String PATH = "task";

    private final TaskService taskService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private TaskDto createUpdateTask(@RequestBody TaskDto taskDto) {
        return taskService.createUpdateTask(taskDto);
    }

    @DeleteMapping
    private void deleteTask(@RequestParam Long taskId) {
        taskService.deleteTask(taskId);
    }

    @PutMapping
    private void markTaskAsDone(@RequestParam Long taskId) {
        taskService.markTaskAsDone(taskId);
    }

    @GetMapping(value = "/open", produces = MediaType.APPLICATION_JSON_VALUE)
    private List<TaskDto> getMyOpenTasks(@RequestParam Long startIndex,
                                         @RequestParam int quantity) {
        return taskService.getMyOpenTasks(startIndex, quantity);
    }

    @GetMapping(value = "/done", produces = MediaType.APPLICATION_JSON_VALUE)
    private List<TaskDto> getMyDoneTasks(@RequestParam Long startIndex,
                                         @RequestParam int quantity) {
        return taskService.getMyDoneTasks(startIndex, quantity);
    }
}
