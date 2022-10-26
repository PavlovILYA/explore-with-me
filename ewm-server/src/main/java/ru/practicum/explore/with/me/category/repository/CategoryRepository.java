package ru.practicum.explore.with.me.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.with.me.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
