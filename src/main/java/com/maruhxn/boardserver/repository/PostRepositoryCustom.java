package com.maruhxn.boardserver.repository;

import com.maruhxn.boardserver.domain.Post;
import com.maruhxn.boardserver.dto.PostSearchCond;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;

public interface PostRepositoryCustom {
    @QueryHints(value = {@QueryHint(name = "org.hibernate.readOnly", value = "true")})
    Page<Post> findAllByConditions(PostSearchCond postSearchCond, Pageable pageable);
}
