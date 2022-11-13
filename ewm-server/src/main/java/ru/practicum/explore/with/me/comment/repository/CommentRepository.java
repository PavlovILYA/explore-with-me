package ru.practicum.explore.with.me.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.with.me.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
