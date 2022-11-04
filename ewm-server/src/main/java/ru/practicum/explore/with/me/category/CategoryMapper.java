package ru.practicum.explore.with.me.category;

import ru.practicum.explore.with.me.category.dto.CategoryDto;
import ru.practicum.explore.with.me.category.model.Category;

public class CategoryMapper {
    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
