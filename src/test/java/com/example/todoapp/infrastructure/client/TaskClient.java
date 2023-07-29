package com.example.todoapp.infrastructure.client;


import com.example.todoapp.domain.model.TaskDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class TaskClient {

    private final MockMvc mvc;

    private final Environment environment;
    private KeycloakClient keycloakClient;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public TaskClient(MockMvc mvc, Environment environment) {
        this.mvc = mvc;
        this.environment = environment;
        keycloakClient = new KeycloakClient(environment);
    }

    public TaskDto createUpdateTask(TaskDto task) throws Exception {
        return createUpdateTaskByUser(task, TestUser.USER);
    }

    public void createUpdateTaskThrows404(TaskDto task) throws Exception {
        mvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task))
                        .header("Authorization", "Bearer " + keycloakClient.getAccessToken(TestUser.USER)))
                .andExpect(status().is(404));
    }

    public TaskDto createUpdateTaskByUser(TaskDto task, TestUser user) throws Exception {
        return objectMapper.readValue(mvc.perform(post("/task")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(task))
                                .header("Authorization", "Bearer " + keycloakClient.getAccessToken(user)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                TaskDto.class);
    }

    public void deleteTask(Long taskId) throws Exception {
        mvc.perform(delete("/task?taskId=" + taskId.toString())
                .header("Authorization", "Bearer " + keycloakClient.getAccessToken(TestUser.USER)));
    }

    public List<TaskDto> getDoneTasks() throws Exception {
        return getDoneTasksPaginated(0L, 50);
    }

    public List<TaskDto> getDoneTasksPaginated(Long startIndex, int quantity) throws Exception {
        String responseJson = mvc.perform(get("/task/done")
                        .queryParam("startIndex", startIndex.toString())
                        .queryParam("quantity", String.valueOf(quantity))
                        .header("Authorization", "Bearer " + keycloakClient.getAccessToken(TestUser.USER)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return List.of(objectMapper.readValue(responseJson, TaskDto[].class));
    }

    public List<TaskDto> getOpenTasks() throws Exception {
        return getOpenTasksPaginated(0L, 50);
    }

    public List<TaskDto> getOpenTasksPaginated(Long startIndex, int quantity) throws Exception {
        String responseJson = mvc.perform(get("/task/open")
                        .queryParam("startIndex", startIndex.toString())
                        .queryParam("quantity", String.valueOf(quantity))
                        .header("Authorization", "Bearer " + keycloakClient.getAccessToken(TestUser.USER)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return List.of(objectMapper.readValue(responseJson, TaskDto[].class));
    }

    public void markTaskAsDone(Long taskId) throws Exception {
        mvc.perform(put("/task?taskId=" + taskId.toString())
                .header("Authorization", "Bearer " + keycloakClient.getAccessToken(TestUser.USER)));
    }
}
