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
        return Optional.ofNullable(em.find(Post.class, postId));
    }

    public Optional<Post> findOneWithAuthorAndImages(Long postId) {
        List<Post> findPost = em.createQuery(
                        "select p from Post p" +
                                " join fetch p.member" +
                                " left join fetch p.images" +
                                " where p.id = :postId", Post.class
                ).setParameter("postId", postId)
                .getResultList();
        return findPost.stream().findFirst();
    }

    public Optional<Post> findOneWithAuthor(Long postId) {
        List<Post> findPost = em.createQuery(
                        "select p from Post p" +
                                " join fetch p.member" +
                                " where p.id = :postId", Post.class
                ).setParameter("postId", postId)
                .getResultList();
        return findPost.stream().findFirst();
    }

    public List<Post> findAllWithMember(Integer page) {
        return em.createQuery(
                        "select p from Post p" +
                                " join fetch p.member", Post.class)
                .setFirstResult(Constants.PAGE_SIZE * page)
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
