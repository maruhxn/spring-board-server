package com.maruhxn.boardserver.repository;

import com.maruhxn.boardserver.common.Constants;
import com.maruhxn.boardserver.domain.Post;
import com.maruhxn.boardserver.domain.PostImage;
import com.maruhxn.boardserver.domain.QComment;
import com.maruhxn.boardserver.dto.PostSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.maruhxn.boardserver.domain.QComment.comment;
import static com.maruhxn.boardserver.domain.QMember.member;
import static com.maruhxn.boardserver.domain.QPost.post;

@Repository
public class PostRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public PostRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

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
//    public List<Post> findAllWithMember(Integer page) {
//        return em.createQuery(
//                        "select p from Post p" +
//                                " join fetch p.member", Post.class)
//                .setFirstResult(Constants.PAGE_SIZE * page)
//                .setMaxResults(Constants.PAGE_SIZE)
//                .getResultList();
//    }

    public List<Post> findAll(PostSearchCond postSearchCond) {
        return query
                .select(post)
                .from(post)
                .join(post.member, member)
                .fetchJoin()
                .leftJoin(post.comments, comment)
                .where(
                        containTitleKeyword(postSearchCond.getTitle()),
                        containContentKeyword(postSearchCond.getContent()),
                        authorLike(postSearchCond.getAuthor()))
                .offset((long) Constants.PAGE_SIZE * postSearchCond.getPage())
                .limit(Constants.PAGE_SIZE)
                .fetch();
    }

    private BooleanExpression containTitleKeyword(String title) {
        if (!StringUtils.hasText(title)) {
            return null;
        }
        return post.title.contains(title);
    }

    private BooleanExpression containContentKeyword(String content) {
        if (!StringUtils.hasText(content)) {
            return null;
        }
        return post.content.contains(content);
    }

    private static BooleanExpression authorLike(String authorName) {
        if (!StringUtils.hasText(authorName)) {
            return null;
        }
        return member.username.like(authorName);
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
