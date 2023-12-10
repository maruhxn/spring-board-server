package com.maruhxn.boardserver.repository;

import com.maruhxn.boardserver.dto.PostSearchCond;
import com.maruhxn.boardserver.dto.response.object.PostItem;
import com.maruhxn.boardserver.dto.response.object.QPostItem;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.maruhxn.boardserver.domain.QComment.comment;
import static com.maruhxn.boardserver.domain.QMember.member;
import static com.maruhxn.boardserver.domain.QPost.post;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public Page<PostItem> findAllByConditions(PostSearchCond postSearchCond, Pageable pageable) {
        List<PostItem> postItems = query
                .select(new QPostItem(
                        post.id,
                        post.title,
                        member.username,
                        post.createdAt,
                        post.viewCount,
                        comment.count()
                ))
                .from(post)
                .leftJoin(post.member, member)
                .leftJoin(post.comments, comment)
                .where(containTitleKeyword(postSearchCond.getTitle()),
                        containContentKeyword(postSearchCond.getContent()),
                        authorLike(postSearchCond.getAuthor()))
                .groupBy(post.id)
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(post.count())
                .from(post)
                .where(containTitleKeyword(postSearchCond.getTitle()),
                        containContentKeyword(postSearchCond.getContent()),
                        authorLike(postSearchCond.getAuthor()));

        return PageableExecutionUtils.getPage(postItems, pageable, countQuery::fetchOne);
    }

    private BooleanExpression containTitleKeyword(String title) {
        return hasText(title) ? post.title.contains(title) : null;
    }

    private BooleanExpression containContentKeyword(String content) {
        return hasText(content) ? post.content.contains(content) : null;
    }

    private static BooleanExpression authorLike(String authorName) {
        return hasText(authorName) ? post.member.username.eq(authorName) : null;
    }
}
