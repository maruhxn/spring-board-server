package com.maruhxn.boardserver.repository;

import com.maruhxn.boardserver.common.Constants;
import com.maruhxn.boardserver.domain.Post;
import com.maruhxn.boardserver.domain.PostImage;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }

    public Optional<Post> findOne(Long postId) {
        List<Post> findPost = em.createQuery(
                        "select p from Post p" +
                                " left join fetch p.images" +
                                " where p.id = :postId", Post.class
                ).setParameter("postId", postId)
                .getResultList();
        return findPost.stream().findFirst();
    }

    public List<Post> findAllWithImages(int page) {
        return em.createQuery(
                        "select p from Post p" +
                                " left join fetch p.images", Post.class)
                .setFirstResult(page)
                .setMaxResults(Constants.PAGE_SIZE)
                .getResultList();
    }

    public void removePost(Post post) {
        em.remove(post);
    }

    public Optional<PostImage> findPostImageById(Long imageId) {
        return Optional.ofNullable(em.find(PostImage.class, imageId));
    }

    public void removeImage(PostImage postImage) {
        em.remove(postImage);
    }
}
