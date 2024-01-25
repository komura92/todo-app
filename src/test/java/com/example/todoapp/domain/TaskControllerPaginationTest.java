package com.example.todoapp.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.example.todoapp.domain.constants.TaskTestConstants;
import com.example.todoapp.domain.validator.TaskDtoValidator;
import com.example.todoapp.domain.model.TaskDto;
import com.example.todoapp.domain.model.TaskStatus;
import com.example.todoapp.infrastructure.BaseApiTest;
import com.example.todoapp.infrastructure.client.TaskClient;

import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

class TaskControllerPaginationTest extends BaseApiTest {

    private TaskClient taskClient;


    private static final List<TaskDto> openTasks = new ArrayList<>();
    private static final List<TaskDto> doneTasks = new ArrayList<>();

    @PostConstruct
    void init() throws Exception {
        taskClient = new TaskClient(mvc, environment);
        prepareData();
    }

    void prepareData() throws Exception {
        if (isNotEmpty(openTasks) && isNotEmpty(doneTasks))
            return;

        for (int i = 0; i < 27; i++) {
            TaskDto createdTask = taskClient.createUpdateTask(TaskTestConstants.CREATE_TASK_DTO);
            openTasks.add(createdTask);
        }

        for (int i = 0; i < 27; i++) {
            TaskDto createdTask = taskClient.createUpdateTask(TaskTestConstants.CREATE_TASK_DTO);
            taskClient.markTaskAsDone(createdTask.id());
            doneTasks.add(createdTask.toBuilder()
                    .status(TaskStatus.DONE)
                    .build());
        }
    }


    @Test
    @Order(1)
    void paginationOnOpenWorks() throws Exception {
        List<TaskDto> firstFiveTasks = taskClient.getOpenTasksPaginated(0L, 5);
        List<TaskDto> secondFiveTasks = taskClient.getOpenTasksPaginated(5L, 5);
        List<TaskDto> firstTenTasks = taskClient.getOpenTasksPaginated(0L, 10);
        List<TaskDto> firstFiftyTasks = taskClient.getOpenTasksPaginated(0L, 50);
        TaskDtoValidator.validatePagination(firstFiveTasks, secondFiveTasks, firstTenTasks, firstFiftyTasks, openTasks);
    }


    @Test
    @Order(2)
    void paginationOnDoneWorks() throws Exception {
        List<TaskDto> firstFiveTasks = taskClient.getDoneTasksPaginated(0L, 5);
        List<TaskDto> secondFiveTasks = taskClient.getDoneTasksPaginated(5L, 5);
        List<TaskDto> firstTenTasks = taskClient.getDoneTasksPaginated(0L, 10);
        List<TaskDto> firstFiftyTasks = taskClient.getDoneTasksPaginated(0L, 50);
        TaskDtoValidator.validatePagination(firstFiveTasks, secondFiveTasks, firstTenTasks, firstFiftyTasks, doneTasks);
    }
}