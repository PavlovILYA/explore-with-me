package ru.practicum.explore.with.me.category.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder(toBuilder = true)
public class CategoryDto {
    private Long id;
    @NotBlank
    private String name;
}
