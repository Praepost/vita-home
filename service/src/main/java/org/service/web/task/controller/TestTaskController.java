package org.service.web.task.controller;

import lombok.RequiredArgsConstructor;
import org.service.web.task.controller.dto.*;
import org.service.web.task.entity.Statuses;
import org.service.web.task.entity.Task;
import org.service.web.task.entity.repository.StatusRepo;
import org.service.web.task.entity.repository.TaskRepo;
import org.service.web.task.exception.DraftException;
import org.service.web.task.exception.StatusException;
import org.service.web.task.exception.UnavailableException;
import org.service.web.user.controller.dto.SuccessResponse;
import org.service.web.user.entity.User;
import org.service.web.user.entity.repository.UserRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class TestTaskController implements ITaskController{

    private final TaskRepo taskRepo;
    private final UserRepo userRepo;
    private final StatusRepo statusRepo;

    @Override
    public SuccessResponse registration(CreateTaskRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.getUserByUsername(username);
        HashSet<User> users = new HashSet<>();
        users.add(user);

        List<Statuses> statuses = new ArrayList<>();
        statuses.add(statusRepo.findByName("черновик"));

        Task task = new Task();
        task.setAuthor(users);
        task.setMessage(request.getMessage());
        task.setStatuses(statuses);
        task.setName(request.getName());
        task.setTimestamp(System.currentTimeMillis());

        taskRepo.save(task);

        return new SuccessResponse("Черновик сохранен");
    }

    @Override
    public SuccessResponse storage(StorageRequest request) {
        List<Statuses> statuses = new ArrayList<>();
        statuses.add(statusRepo.findByName("отправлено"));

        Task task = taskRepo.findByName(request.getName());
        if (task.getStatuses().stream()
                .map(v->v.getName())
                .collect(Collectors.toList()).contains(("черновик"))) {
            task.setStatuses(statuses);
        } else throw new StatusException("У заявки статус не отправленно");
        taskRepo.save(task);

        return new SuccessResponse("Успешно отправленно");
    }

    @Override
    public SuccessResponse edit(EditRequest request) {

        Task task = taskRepo.findByName(request.getName());
        if (task.getStatuses().stream()
                .map(v->v.getName())
                .collect(Collectors.toList()).contains(("черновик"))) {
            task.setMessage(request.getMessage());
            task.setName(request.getName());
        } else throw new DraftException("У заявки статус не черновик");

        return new SuccessResponse("Успешно обновленно");
    }

    @Override
    public TaskResponse look(TaskRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.getUserByUsername(username);
        String result = "";

        Task task = taskRepo.findByName(request.getName());
        task.getStatuses();
        String message = task.getMessage();

        if (user.getRoles().stream().map(v -> v.getName())
                .collect(Collectors.toList()).contains(("Пользователь"))){
            result = message;
        } else if (user.getRoles().stream().map(v -> v.getName())
                .collect(Collectors.toList()).contains(("Оператор")))
            if (task.getStatuses().stream()
                    .map(v -> v.getName())
                    .collect(Collectors.toList()).contains(("отправлено"))) {
                char[] chars = message.toCharArray();
                for (char c : chars) {
                    result += String.valueOf(c) + "-";
                }
            }
        return new TaskResponse(task.getId(), task.getName(), result, task.getAuthor().stream().findFirst().get().getUsername(), task.getTimestamp());
    }

    @Override
    public TasksResponse lookAuthor(TasksRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.getUserByUsername(username);

        Pageable pages = null;
        List<Task> tasks = new ArrayList<>();

        if(user.getRoles().stream().map(v -> v.getName())
                .collect(Collectors.toList()).contains(("Оператор"))){
            user = userRepo.getUserByUsername(request.getUser());
        } else if(user.getRoles().stream().map(v -> v.getName())
                .collect(Collectors.toList()).contains(("Пользователь"))){
            if(request.getDesc()){
                pages = PageRequest.of(request.getPage(), 5, Sort.by("timestamp").descending());
            } else {
                pages = PageRequest.of(request.getPage(), 5, Sort.by("timestamp"));
            }
            tasks = taskRepo.findTasksByAuthor(user, pages);
        } else {
            throw new UnavailableException("Администратор не имеет прав");
        }

        return new TasksResponse(tasks);
    }

    @Override
    public TasksResponse lookAll(AllTaskRequest request) {

        Pageable pages = null;
        if(request.getDesc()){
            pages = PageRequest.of(request.getPage(), 5, Sort.by("timestamp").descending());
        } else {
            pages = PageRequest.of(request.getPage(), 5, Sort.by("timestamp"));
        }

        //todo bug
        List<Task> tasks = taskRepo.findTasksByStatuses("«отправлено»", pages);


        return new TasksResponse(tasks);
    }

    @Override
    public SuccessResponse update(ChangeStatusRequest request) {
        List<Statuses> statuses = new ArrayList<>();
        statuses.add(statusRepo.findByName(request.getStatus()));

        Task task = taskRepo.findByName(request.getName());

        task.setStatuses(statuses);
        taskRepo.save(task);

        return new SuccessResponse("Успешно обновлен статус");
    }
}
