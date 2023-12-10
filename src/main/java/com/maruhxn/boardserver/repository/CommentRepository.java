package com.maruhxn.boardserver.repository;

import com.maruhxn.boardserver.domain.Comment;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @QueryHints(value = {@QueryHint(name = "org.hibernate.readOnly", value = "true")}, forCounting = true)
    @EntityGraph(attributePaths = {"member"})
    Page<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
}
