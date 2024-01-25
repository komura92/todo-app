package com.example.todoapp.domain;

import java.util.List;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.example.todoapp.domain.constants.TaskTestConstants;
import com.example.todoapp.domain.validator.TaskDtoValidator;
import com.example.todoapp.domain.model.TaskDto;
import com.example.todoapp.infrastructure.BaseApiTest;
import com.example.todoapp.infrastructure.client.TaskClient;
import com.example.todoapp.infrastructure.client.TestUser;

import static org.assertj.core.api.Assertions.assertThat;


class TaskControllerApiTest extends BaseApiTest {

    private TaskClient taskClient;

    @PostConstruct
    void init() {
        taskClient = new TaskClient(mvc, environment);
    }

    @Test
    @Order(1)
    void userCanCreateTask() throws Exception {
        TaskDto taskToCreate = TaskTestConstants.CREATE_TASK_DTO;
        TaskDto createdTask = taskClient.createUpdateTask(taskToCreate);
        TaskDtoValidator.validateCreate(taskToCreate, createdTask);
    }

    @Test
    @Order(2)
    void userCanUpdateTask() throws Exception {
        taskClient.createUpdateTask(TaskTestConstants.CREATE_TASK_DTO);
        TaskDto taskToUpdate = TaskTestConstants.UPDATE_TASK_DTO;
        TaskDto responseTask = taskClient.createUpdateTask(taskToUpdate);
        TaskDtoValidator.validateUpdate(taskToUpdate, responseTask);
    }

    @Test
    @Order(3)
    void userCanMarkTaskAsDone() throws Exception {
        List<TaskDto> openTasks = taskClient.getOpenTasks();
        List<TaskDto> doneTasks = taskClient.getDoneTasks();
        taskClient.markTaskAsDone(1L);
        taskClient.markTaskAsDone(2L);

        List<TaskDto> openTasksAfterMarking = taskClient.getOpenTasks();
        List<TaskDto> doneTasksAfterMarking = taskClient.getDoneTasks();

        TaskDtoValidator.validateMarkingAsDone(openTasks, doneTasks, openTasksAfterMarking, doneTasksAfterMarking);
    }

    @Test
    @Order(4)
    void userCanGetDoneTasks() throws Exception {
        List<TaskDto> doneTasks = taskClient.getDoneTasks();
        assertThat(doneTasks)
                .describedAs("There should be two done tasks")
                .hasSize(2);
    }

    @Test
    @Order(5)
    void userCanGetOpenTasks() throws Exception {
        for (int i = 0; i < 10; i++)
            taskClient.createUpdateTask(TaskTestConstants.CREATE_TASK_DTO);
        taskClient.createUpdateTask(TaskTestConstants.CREATE_TASK_DTO);
        List<TaskDto> openTasks = taskClient.getOpenTasks();
        assertThat(openTasks)
                .describedAs("There should be nine open tasks")
                .hasSize(11);
    }

    @Test
    @Order(6)
    void userCanDeleteOpenTask() throws Exception {
        TaskDto taskToDelete = taskClient.createUpdateTask(TaskTestConstants.CREATE_TASK_DTO);
        List<TaskDto> doneTasks = taskClient.getOpenTasks();
        taskClient.deleteTask(taskToDelete.id());
        List<TaskDto> doneTasksAfterDelete = taskClient.getOpenTasks();
        assertThat(doneTasks.size() - 1)
                .describedAs("Task was not deleted")
                .isEqualTo(doneTasksAfterDelete.size());
    }

    @Test
    @Order(7)
    void userCantDeleteDoneTask() throws Exception {
        List<TaskDto> doneTasks = taskClient.getOpenTasks();
        taskClient.deleteTask(2L);
        List<TaskDto> doneTasksAfterDelete = taskClient.getOpenTasks();
        assertThat(doneTasks.size())
                .describedAs("Task was deleted")
                .isEqualTo(doneTasksAfterDelete.size());
    }

    @Test
    @Order(8)
    void userCantSetPredefinedValue() throws Exception {
        TaskDto taskToCreate = TaskTestConstants.CREATE_TASK_WITH_PREDEFINED_DTO;
        TaskDto createdTask = taskClient.createUpdateTask(taskToCreate);
        TaskDtoValidator.validatePredefined(taskToCreate, createdTask);
    }

    @Test
    @Order(9)
    void userCannotEditSomebodysTask() throws Exception {
        taskClient.createUpdateTaskByUser(TaskTestConstants.CREATE_TASK_DTO, TestUser.USER_2);
        taskClient.createUpdateTaskThrows404(TaskTestConstants.USER_2_TASK_UPDATE);
    }

    @Test
    @Order(10)
    void userGets404OnMarkingAsDoneNotExistingTask() throws Exception {
        taskClient.markNotExistingTaskAsDoneReturns404(-1L);
    }
}