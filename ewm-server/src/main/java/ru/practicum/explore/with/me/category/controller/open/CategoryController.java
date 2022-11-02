package ru.practicum.explore.with.me.category.controller.open;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.category.CategoryMapper;
import ru.practicum.explore.with.me.category.dto.CategoryDto;
import ru.practicum.explore.with.me.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/categories")
@RestController("publicCategoryController")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable("catId") Long categoryId) {
        log.info("GET /categories/{}", categoryId);
        return CategoryMapper.toCategoryDto(
                categoryService.getCategory(categoryId));
    }

    @GetMapping
    public List<CategoryDto> getCategories(@PositiveOrZero
                                           @RequestParam(value = "from", defaultValue = "0") int from,
                                           @Positive
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("GET /categories from={} size={}", from, size);
        return categoryService.getCategories(from, size).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}
