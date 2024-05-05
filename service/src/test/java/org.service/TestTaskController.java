package org.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.service.web.task.controller.dto.*;
import org.service.web.task.entity.Task;
import org.service.web.task.entity.repository.StatusRepo;
import org.service.web.task.entity.repository.TaskRepo;
import org.service.web.user.entity.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
@Slf4j
public class TestTaskController {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private StatusRepo statusRepo;

    @Test
    @WithMockUser(username="Пользователь",roles={"Пользователь"})
    public void createTaskRequestSuccess() {
        Task task = null;

        try {

            CreateTaskRequest createTaskRequest =
                    new CreateTaskRequest("name", "message");

            mockMvc.perform(post("/create/")
                    .content(objectMapper.writeValueAsString(createTaskRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());

            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            task = taskRepo.findByName("name");

            assertEquals(task.getMessage(), createTaskRequest.getMessage());
            assertEquals(task.getName(), createTaskRequest.getName());
            assertEquals(task.getAuthor(), username);


        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            log.info("finally");
            taskRepo.deleteAll();

        }
    }

    @Test
    @WithMockUser(username="Пользователь",roles={"Пользователь"})
    public void storageTaskRequestSuccess() throws Exception {
        try {

            CreateTaskRequest createTaskRequest =
                new CreateTaskRequest("name", "message");

        mockMvc.perform(post("/create/")
                .content(objectMapper.writeValueAsString(createTaskRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        StorageRequest storageRequest =
                new StorageRequest("name");

        mockMvc.perform(post("/storage/")
                .content(objectMapper.writeValueAsString(storageRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        Task task = taskRepo.findByName(storageRequest.getName());

        assertEquals(storageRequest.getName(), task.getName());

        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            log.info("finally");
            taskRepo.deleteAll();

        }
    }

    @Test
    public void editTaskRequestSuccess() throws Exception {

        try {


        CreateTaskRequest createTaskRequest =
                new CreateTaskRequest("name", "message");

        mockMvc.perform(post("/create/")
                .content(objectMapper.writeValueAsString(createTaskRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("Пользователь").password("Пользователь").roles("Пользователь"))
        ).andExpect(status().isOk());


        EditRequest editRequest =
                new EditRequest("name", "message");

        mockMvc.perform(post("/storage/")
                .content(objectMapper.writeValueAsString(editRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("Пользователь").password("Пользователь").roles("Пользователь"))
        ).andExpect(status().isOk());

        Task task = taskRepo.findByName(editRequest.getName());

        assertEquals(editRequest.getName(), task.getName());
        assertEquals(editRequest.getMessage(), task.getMessage());

        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            log.info("finally");
            taskRepo.deleteAll();

        }
    }

    @Test
    public void lookTaskRequestOperatorSuccess() throws Exception {
        try {

        CreateTaskRequest createTaskRequest =
                new CreateTaskRequest("name", "message");

        mockMvc.perform(post("/create/")
                .content(objectMapper.writeValueAsString(createTaskRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("Пользователь").password("Пользователь").roles("Пользователь"))
        ).andExpect(status().isOk());


        StorageRequest storageRequest =
                new StorageRequest("name");

        mockMvc.perform(post("/storage/")
                .content(objectMapper.writeValueAsString(storageRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("Пользователь").password("Пользователь").roles("Пользователь"))
        ).andExpect(status().isOk());


        TaskRequest taskRequest = new TaskRequest("name");

        String taskResponse = mockMvc.perform(post("/look/")
                .content(objectMapper.writeValueAsString(taskRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("Оператор").password("Оператор").roles("Оператор"))
        ).andReturn().getResponse().getContentAsString();

        Task task = taskRepo.findByName(storageRequest.getName());

        assertEquals(true, taskResponse.contains("m-e-s-s-a-g-e"));

        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            log.info("finally");
            taskRepo.deleteAll();

        }
    }

    @Test
    public void lookTaskRequestUserSuccess() throws Exception {
        try{

        CreateTaskRequest createTaskRequest =
                new CreateTaskRequest("name", "message2");

        mockMvc.perform(post("/create/")
                .content(objectMapper.writeValueAsString(createTaskRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("Пользователь").password("Пользователь").roles("Пользователь"))
        ).andExpect(status().isOk());


        StorageRequest storageRequest =
                new StorageRequest("name");

        mockMvc.perform(post("/storage/")
                .content(objectMapper.writeValueAsString(storageRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("Пользователь").password("Пользователь").roles("Пользователь"))
        ).andExpect(status().isOk());


        TaskRequest taskRequest = new TaskRequest("name");

        String taskResponse = mockMvc.perform(post("/look/")
                .content(objectMapper.writeValueAsString(taskRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("Пользователь").password("Пользователь").roles("Пользователь"))
        ).andReturn().getResponse().getContentAsString();

        Task task = taskRepo.findByName(storageRequest.getName());

        assertEquals(true, taskResponse.contains("message2"));

        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            log.info("finally");
            taskRepo.deleteAll();

        }
    }
    @Test
    public void lookTaskAuthorRequestUserSuccess() throws Exception {

        try {

        CreateTaskRequest createTaskRequest =
                new CreateTaskRequest("name", "message");

        mockMvc.perform(post("/create/")
                .content(objectMapper.writeValueAsString(createTaskRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("Пользователь").password("Пользователь").roles("Пользователь"))
        ).andExpect(status().isOk());


        StorageRequest storageRequest =
                new StorageRequest("name");

//        mockMvc.perform(post("/storage/")
//                .content(objectMapper.writeValueAsString(storageRequest))
//                .contentType(MediaType.APPLICATION_JSON)
//                .with(user("Пользователь").password("Пользователь").roles("Пользователь"))
//        ).andExpect(status().isOk());


        TasksRequest taskRequest = new TasksRequest(0, true, "Пользователь");

        String tasksResponse = mockMvc.perform(post("/look/author/")
                .content(objectMapper.writeValueAsString(taskRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("Пользователь").password("Пользователь").roles("Пользователь"))
        ).andReturn().getResponse().getContentAsString();

        Task task = taskRepo.findByName(storageRequest.getName());

        assertEquals(true, tasksResponse.contains("name"));
        assertEquals(true, tasksResponse.contains("message"));


        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            log.info("finally");
            taskRepo.deleteAll();

        }
    }

    @Test
    public void lookTaskAuthorRequestUserAllSuccess() throws Exception {

        try {

            CreateTaskRequest createTaskRequest =
                    new CreateTaskRequest("name", "message");

            mockMvc.perform(post("/create/")
                    .content(objectMapper.writeValueAsString(createTaskRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user("Пользователь").password("Пользователь").roles("Пользователь"))
            ).andExpect(status().isOk());

            StorageRequest storageRequest = new StorageRequest("name");

                    mockMvc.perform(post("/storage/")
                    .content(objectMapper.writeValueAsString(storageRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user("Пользователь").password("Пользователь").roles("Пользователь"))
            ).andExpect(status().isOk());

            TasksRequest taskRequest = new TasksRequest(0, true, "Пользователь");

            String tasksResponse = mockMvc.perform(get("/look/all/")
                    .content(objectMapper.writeValueAsString(taskRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user("Оператор").password("Оператор").roles("Оператор"))
            ).andReturn().getResponse().getContentAsString();

            Task task = taskRepo.findByName(storageRequest.getName());

            assertEquals(true, tasksResponse.contains("name"));
            assertEquals(true, tasksResponse.contains("message"));


        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            log.info("finally");
            taskRepo.deleteAll();

        }
    }

    @Test
    public void lookTaskAuthorRequestUserUpdate() throws Exception {

        try {

            CreateTaskRequest createTaskRequest =
                    new CreateTaskRequest("name", "message");

            mockMvc.perform(post("/create/")
                    .content(objectMapper.writeValueAsString(createTaskRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user("Пользователь").password("Пользователь").roles("Пользователь"))
            ).andExpect(status().isOk());

            StorageRequest storageRequest = new StorageRequest("name");

            mockMvc.perform(post("/storage/")
                    .content(objectMapper.writeValueAsString(storageRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user("Пользователь").password("Пользователь").roles("Пользователь"))
            ).andExpect(status().isOk());

            ChangeStatusRequest ChangeStatusRequest = new ChangeStatusRequest("name2", "status2");

            String tasksResponse = mockMvc.perform(post("/update/")
                    .content(objectMapper.writeValueAsString(ChangeStatusRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user("Оператор").password("Оператор").roles("Оператор"))
            ).andReturn().getResponse().getContentAsString();

            Task task = taskRepo.findByName(storageRequest.getName());

            assertEquals(true, tasksResponse.contains("name2"));
            assertEquals(true, tasksResponse.contains("message2"));


        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            log.info("finally");
            taskRepo.deleteAll();

        }
    }
}
