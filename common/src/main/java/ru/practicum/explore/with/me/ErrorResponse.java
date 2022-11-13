package ru.practicum.explore.with.me;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorResponse {
    private String status;
    private String reason;
    private String message;
    private List<String> errors;
    private String timestamp;
}
