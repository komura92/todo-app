package com.example.todoapp.domain;

import com.example.todoapp.domain.constants.TaskTestConstants;
import com.example.todoapp.infrastructure.client.KeycloakClient;
import com.example.todoapp.infrastructure.client.TestUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class TaskControllerAuthorizationTest {


    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mvc;

    @Autowired
    private Environment environment;

    private KeycloakClient keycloakClient;

    @PostConstruct
    public void init() {
        keycloakClient = new KeycloakClient(environment);
    }

    @Test
    void authorizedCanCreateUpdateTask() throws Exception {
        mvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(getObjectAsString(TaskTestConstants.CREATE_TASK_DTO))
                        .header("Authorization", "Bearer " + keycloakClient.getAccessToken(TestUser.USER)))
                .andExpect(status().is(200));
    }

    private String getObjectAsString(Object object) throws JsonProcessingException {
        return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(object);
    }

    @Test
    void unauthorizedCannotCreateUpdateTask() throws Exception {
        mvc.perform(post("/task"))
                .andExpect(status().is(401));
    }

    @Test
    void authorizedCanGetOpenTasks() throws Exception {
        mvc.perform(get("/task/open")
                        .queryParam("startIndex", "0")
                        .queryParam("quantity", "50")
                        .header("Authorization", "Bearer " + keycloakClient.getAccessToken(TestUser.USER)))
                .andExpect(status().is(200));
    }

    @Test
    void unauthorizedCannotGetOpenTasks() throws Exception {
        mvc.perform(get("/task/open")
                        .queryParam("startIndex", "0")
                        .queryParam("quantity", "50"))
                .andExpect(status().is(401));
    }

    @Test
    void authorizedCanGetDoneTasks() throws Exception {
        mvc.perform(get("/task/done")
                        .queryParam("startIndex", "0")
                        .queryParam("quantity", "50")
                        .header("Authorization", "Bearer " + keycloakClient.getAccessToken(TestUser.USER)))
                .andExpect(status().is(200));
    }

    @Test
    void unauthorizedCannotGetDoneTasks() throws Exception {
        mvc.perform(get("/task/done")
                        .queryParam("startIndex", "0")
                        .queryParam("quantity", "50"))
                .andExpect(status().is(401));
    }

    @Test
    void authorizedCanDeleteTask() throws Exception {
        mvc.perform(delete("/task?taskId=-1")
                        .header("Authorization", "Bearer " + keycloakClient.getAccessToken(TestUser.USER)))
                .andExpect(status().is(404));
    }

    @Test
    void unauthorizedCannotDeleteTask() throws Exception {
        mvc.perform(delete("/task?taskId=1"))
                .andExpect(status().is(401));
    }

    @Test
    void authorizedCanMarkTaskAsDone() throws Exception {
        mvc.perform(put("/task?taskId=-1")
                        .header("Authorization", "Bearer " + keycloakClient.getAccessToken(TestUser.USER)))
                .andExpect(status().is(404));
    }

    @Test
    void unauthorizedCannotMarkTaskAsDone() throws Exception {
        mvc.perform(delete("/task?taskId=1"))
                .andExpect(status().is(401));
    }
}