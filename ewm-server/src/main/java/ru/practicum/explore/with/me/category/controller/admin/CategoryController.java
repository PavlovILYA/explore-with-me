package ru.practicum.explore.with.me.category.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.category.CategoryMapper;
import ru.practicum.explore.with.me.category.dto.CategoryDto;
import ru.practicum.explore.with.me.category.model.Category;
import ru.practicum.explore.with.me.category.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@RestController("AdminCategoryController")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto saveCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("POST /admin/categories categoryDto: {}", categoryDto);
        Category category = CategoryMapper.toCategory(categoryDto);
        return CategoryMapper.toCategoryDto(
                categoryService.saveCategory(category));
    }

    @PatchMapping
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("PATCH /admin/categories categoryDto: {}", categoryDto);
        Category category = CategoryMapper.toCategory(categoryDto);
        return CategoryMapper.toCategoryDto(
                categoryService.updateCategory(category));
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        log.info("DELETE /admin/categories/{}", categoryId);
        categoryService.deleteCategory(categoryId);
    }
}
