package com.example.todoapp.domain.validator;

import com.example.todoapp.domain.model.TaskDto;
import com.example.todoapp.domain.model.TaskStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.assertj.core.api.SoftAssertions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskDtoValidator {

    public static void validateCreate(TaskDto taskToCreate, TaskDto createdTask) {
        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(createdTask.getId())
                .describedAs("ID is not set")
                .isNotNull();

        assertions.assertThat(taskToCreate.getName())
                .describedAs("Name doesn't match")
                .isEqualTo(createdTask.getName());

        assertions.assertThat(taskToCreate.getDescription())
                .describedAs("Description doesn't match")
                .isEqualTo(createdTask.getDescription());

        assertions.assertThat(taskToCreate.getPriority())
                .describedAs("Priority doesn't match")
                .isEqualTo(createdTask.getPriority());

        assertions.assertThat(taskToCreate.getDeadline())
                .describedAs("Deadline doesn't match")
                .isEqualTo(createdTask.getDeadline());

        assertions.assertThat(createdTask.getStatus())
                .describedAs("Status not set properly")
                .isEqualTo(TaskStatus.OPEN);

        assertions.assertAll();
    }

    public static void validateUpdate(TaskDto taskToCreate, TaskDto createdTask) {
        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(taskToCreate.getId())
                .describedAs("ID doesn't match")
                .isEqualTo(createdTask.getId());

        assertions.assertThat(taskToCreate.getName())
                .describedAs("Name doesn't match")
                .isEqualTo(createdTask.getName());

        assertions.assertThat(taskToCreate.getDescription())
                .describedAs("Description doesn't match")
                .isEqualTo(createdTask.getDescription());

        assertions.assertThat(taskToCreate.getPriority())
                .describedAs("Priority doesn't match")
                .isEqualTo(createdTask.getPriority());

        assertions.assertThat(taskToCreate.getDeadline())
                .describedAs("Deadline doesn't match")
                .isEqualTo(createdTask.getDeadline());

        assertions.assertAll();
    }

    public static void validatePredefined(TaskDto taskToCreate, TaskDto createdTask) {
        assertThat(taskToCreate.getStatus())
                .describedAs("Status can be set user side")
                .isNotEqualTo(createdTask.getStatus());
    }

    public static void validateMarkingAsDone(List<TaskDto> openTasks,
                                             List<TaskDto> doneTasks,
                                             List<TaskDto> openTasksAfterMarking,
                                             List<TaskDto> doneTasksAfterMarking) {
        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(openTasks)
                .describedAs("Open tasks quantity not reduced after marking task done")
                .hasSize(openTasksAfterMarking.size() + 2);
        assertions.assertThat(doneTasksAfterMarking)
                .describedAs("Done tasks quantity not increased after marking task done")
                .hasSize(doneTasks.size() + 2);

        assertions.assertAll();
    }

    public static void validatePagination(List<TaskDto> firstFiveTasks,
                                          List<TaskDto> secondFiveTasks,
                                          List<TaskDto> firstTenTasks,
                                          List<TaskDto> firstFiftyTasks,
                                          List<TaskDto> allTasks) {
        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(firstFiveTasks)
                .describedAs("Incorrect number of open tasks")
                .hasSize(5);

        assertions.assertThat(firstFiveTasks)
                .describedAs("Incorrect order of open tasks")
                .isEqualTo(allTasks.subList(0, 5));

        assertions.assertThat(secondFiveTasks)
                .describedAs("Incorrect number of open tasks")
                .hasSize(5);

        assertions.assertThat(secondFiveTasks)
                .describedAs("Incorrect order of open tasks")
                .isEqualTo(allTasks.subList(5, 10));

        assertions.assertThat(firstTenTasks)
                .describedAs("Incorrect number of open tasks")
                .hasSize(10);

        assertions.assertThat(firstTenTasks)
                .describedAs("Incorrect order of open tasks")
                .isEqualTo(allTasks.subList(0, 10));

        assertions.assertThat(firstFiftyTasks)
                .describedAs("Incorrect number of open tasks")
                .hasSize(allTasks.size());

        assertions.assertAll();
    }
}
