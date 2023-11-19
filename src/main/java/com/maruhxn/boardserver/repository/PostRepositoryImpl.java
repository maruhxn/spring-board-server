package com.maruhxn.boardserver.repository;

import com.maruhxn.boardserver.domain.Post;
import com.maruhxn.boardserver.dto.PostSearchCond;
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

    // BUG: where 문에 오류가있는건지.. member에 대한 in 쿼리가 나가고 있다.. -> LAZY 세팅이 안 되어있었네!!
    @Override
    public Page<Post> findAllByConditions(PostSearchCond postSearchCond, Pageable pageable) {
        List<Post> posts = query.selectFrom(post)
                .leftJoin(post.member, member).fetchJoin()
                .leftJoin(post.comments, comment) // 지연 로딩 + batch_size
                .where(containTitleKeyword(postSearchCond.getTitle()),
                        containContentKeyword(postSearchCond.getContent()),
                        authorLike(postSearchCond.getAuthor()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(post.count())
                .from(post)
                .where(containTitleKeyword(postSearchCond.getTitle()),
                        containContentKeyword(postSearchCond.getContent()),
                        authorLike(postSearchCond.getAuthor()));

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
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
