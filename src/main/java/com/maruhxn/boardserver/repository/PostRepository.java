package com.maruhxn.boardserver.repository;

import com.maruhxn.boardserver.domain.Post;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    @QueryHints(value = {@QueryHint(name = "org.hibernate.readOnly", value = "true")})
    @EntityGraph(attributePaths = {"member", "images"})
    Optional<Post> findWithMemberAndImagesFirstById(Long postId);

    @QueryHints(value = {@QueryHint(name = "org.hibernate.readOnly", value = "true")})
    @EntityGraph(attributePaths = {"member"})
    Optional<Post> findWithMemberFirstById(Long postId);
}
