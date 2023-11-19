package com.maruhxn.boardserver.repository;

import com.maruhxn.boardserver.dto.PostSearchCond;
import com.maruhxn.boardserver.dto.response.object.PostItem;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;

public interface PostRepositoryCustom {
    @QueryHints(value = {@QueryHint(name = "org.hibernate.readOnly", value = "true")})
    Page<PostItem> findAllByConditions(PostSearchCond postSearchCond, Pageable pageable);
}
