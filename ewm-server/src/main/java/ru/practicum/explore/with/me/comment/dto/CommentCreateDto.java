package ru.practicum.explore.with.me.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentCreateDto {
    @NotBlank
    private String text;
}
