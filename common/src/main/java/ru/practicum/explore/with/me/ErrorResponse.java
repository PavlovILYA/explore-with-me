package ru.practicum.explore.with.me;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String status;
    private String reason;
    private String message;
    private List<String> errors;
    private String timestamp;
}
