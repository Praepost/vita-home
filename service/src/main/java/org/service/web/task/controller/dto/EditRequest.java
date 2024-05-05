package org.service.web.task.controller.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Validated
public class EditRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String message;
}