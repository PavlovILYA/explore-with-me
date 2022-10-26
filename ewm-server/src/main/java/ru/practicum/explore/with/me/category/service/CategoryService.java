package ru.practicum.explore.with.me.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.category.exception.CategoryNotFoundException;
import ru.practicum.explore.with.me.category.model.Category;
import ru.practicum.explore.with.me.category.repository.CategoryRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category saveCategory(Category category) {
        Category savedCategory = categoryRepository.save(category);
        log.info("Saved category: {}", savedCategory);
        return savedCategory;
    }

    public Category updateCategory(Category category) {
        Category categoryFromDb = getCategory(category.getId());
        categoryFromDb.setName(category.getName()); // does it work?
        log.info("Updated category: {}", categoryFromDb);
        return categoryFromDb;
    }

    public void deleteCategory(Long categoryId) {
        Category categoryFromDb = getCategory(categoryId);
        categoryRepository.delete(categoryFromDb);
        log.info("Category {} is deleted", categoryId);
    }

    public Category getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        log.info("Get category {}", category);
        return category;
    }

    public List<Category> getCategories(int from, int size) {
        Pageable pageRequest = PageRequest.of(from / size, size);
        List<Category> categories = categoryRepository.findAll(pageRequest).getContent();
        log.info("Get: {}", categories);
        return categories;
    }
}
