package com.maruhxn.boardserver.repository;

import com.maruhxn.boardserver.common.Constants;
import com.maruhxn.boardserver.domain.Comment;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }

    public Optional<Comment> findOne(Long commentId) {
        return Optional.ofNullable(em.find(Comment.class, commentId));
    }

    public List<Comment> findAll(Long postId, int page) {
        return em.createQuery(
                        "select c from Comment c" +
                                " join fetch c.member" +
                                " where c.post.id = :postId", Comment.class
                ).setParameter("postId", postId)
                .setFirstResult(page)
                .setMaxResults(Constants.PAGE_SIZE)
                .getResultList();
    }

    public void removeOne(Comment comment) {
        em.remove(comment);
    }
}
