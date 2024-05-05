package org.service.web.task.controller;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.service.web.exception.dto.MessagesResponse;
import org.service.web.exception.dto.SuccessfulResponse;
import org.service.web.task.controller.dto.*;
import org.service.web.user.controller.dto.SuccessResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Validated
public interface ITaskController {
    @PreAuthorize("hasRole('Пользователь')")
    @Transactional
    @PostMapping("/create/")
    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400,  response = MessagesResponse.class, message =
                    "Ошибка при создании "),
    })
    @ApiModelProperty(
            value = "first name of the user",
            name = "firstName",
            dataType = "String",
            example = "Vatsal")
    SuccessResponse registration(@Valid @RequestBody CreateTaskRequest request);

    @PreAuthorize("hasRole('Пользователь')")
    @Transactional
    @PostMapping("/storage/")
    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400,  response = MessagesResponse.class, message =
                    "Ошибка при отправке "),
    })
    SuccessResponse storage(@Valid @RequestBody StorageRequest request);

    @PreAuthorize("hasRole('Пользователь')")
    @Transactional
    @PostMapping("/edit/")
    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400,  response = MessagesResponse.class, message =
                    "Ошибка при редактировании "),
    })
    SuccessResponse edit(@Valid @RequestBody EditRequest request);

//    @PreAuthorize("hasAnyRole('Пользователь', 'Администратор')")

    @PreAuthorize("hasAnyRole('Пользователь', 'Оператор')")
    @Transactional
    @PostMapping("/look/")
    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400,  response = MessagesResponse.class, message =
                    "Ошибка при просмотре "),
    })
    TaskResponse look(@Valid @RequestBody TaskRequest request);

    @PreAuthorize("hasAnyRole('Пользователь', 'Оператор')")
    @Transactional
    @PostMapping("/look/author/")
    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400,  response = MessagesResponse.class, message =
                    "Ошибка при просмотре "),
    })
    TasksResponse lookAuthor(@Valid @RequestBody TasksRequest request);

    @PreAuthorize("hasRole('Оператор')")
    @Transactional
    @GetMapping("/look/all/")
    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400,  response = MessagesResponse.class, message =
                    "Ошибка при просмотре "),
    })
    TasksResponse lookAll(@Valid @RequestBody AllTaskRequest request);

    @PreAuthorize("hasRole('Оператор')")
    @Transactional
    @PostMapping("/update/")
    @ApiResponses({
            @ApiResponse(code = 200, response = SuccessfulResponse.class, message =
                    "Успешный ответ: "),
            @ApiResponse(code = 400,  response = MessagesResponse.class, message =
                    "Ошибка при обновлени статуса"),
    })
    SuccessResponse update(@Valid @RequestBody ChangeStatusRequest request);
}
