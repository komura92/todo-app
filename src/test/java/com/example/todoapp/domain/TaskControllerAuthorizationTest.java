package com.example.todoapp.domain;

import com.example.todoapp.domain.constants.TaskTestConstants;
import com.example.todoapp.infrastructure.BaseApiTest;
import com.example.todoapp.infrastructure.client.KeycloakClient;
import com.example.todoapp.infrastructure.client.TestUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class TaskControllerAuthorizationTest extends BaseApiTest {

    private KeycloakClient keycloakClient;

    @PostConstruct
    public void init() {
        keycloakClient = new KeycloakClient(environment);
    }

    @Test
    @Order(1)
    void authorizedCanCreateUpdateTask() throws Exception {
        mvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(getObjectAsString(TaskTestConstants.CREATE_TASK_DTO))
                        .header("Authorization", "Bearer " + keycloakClient.getAccessToken(TestUser.USER)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200));
    }

    private String getObjectAsString(Object object) throws JsonProcessingException {
        return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(object);
    }

    @Test
    @Order(2)
    void unauthorizedCannotCreateUpdateTask() throws Exception {
        mvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(getObjectAsString(TaskTestConstants.CREATE_TASK_DTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(3)
    void authorizedCanGetOpenTasks() throws Exception {
        mvc.perform(get("/task/open")
                        .queryParam("startIndex", "0")
                        .queryParam("quantity", "50")
                        .header("Authorization", "Bearer " + keycloakClient.getAccessToken(TestUser.USER)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200));
    }

    @Test
    @Order(4)
    void unauthorizedCannotGetOpenTasks() throws Exception {
        mvc.perform(get("/task/open")
                        .queryParam("startIndex", "0")
                        .queryParam("quantity", "50"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(5)
    void authorizedCanGetDoneTasks() throws Exception {
        mvc.perform(get("/task/done")
                        .queryParam("startIndex", "0")
                        .queryParam("quantity", "50")
                        .header("Authorization", "Bearer " + keycloakClient.getAccessToken(TestUser.USER)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200));
    }

    @Test
    @Order(6)
    void unauthorizedCannotGetDoneTasks() throws Exception {
        mvc.perform(get("/task/done")
                        .queryParam("startIndex", "0")
                        .queryParam("quantity", "50"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(7)
    void authorizedCanDeleteTask() throws Exception {
        mvc.perform(delete("/task?taskId=-1")
                        .header("Authorization", "Bearer " + keycloakClient.getAccessToken(TestUser.USER)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }

    @Test
    @Order(8)
    void unauthorizedCannotDeleteTask() throws Exception {
        mvc.perform(delete("/task?taskId=-1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(9)
    void authorizedCanMarkTaskAsDone() throws Exception {
        mvc.perform(put("/task?taskId=-1")
                        .header("Authorization", "Bearer " + keycloakClient.getAccessToken(TestUser.USER)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }

    @Test
    @Order(10)
    void unauthorizedCannotMarkTaskAsDone() throws Exception {
        mvc.perform(delete("/task?taskId=1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }
}
