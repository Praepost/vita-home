package org.service.web.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.service.web.exception.dto.SuccessfulResponse;

@Data
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
public class SuccessResponse extends SuccessfulResponse {

    private String message;
}
