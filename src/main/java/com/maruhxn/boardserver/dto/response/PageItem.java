package com.maruhxn.boardserver.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class PageItem<T> {
    private List<T> results;
    private Boolean isFirst;
    private Boolean isLast;
    private Boolean isEmpty;
    private int totalPage;
    private Long totalElements;

    public static <T> PageItem fromPage(Page<T> page) {
        return PageItem.builder()
                .results(Collections.singletonList(page.getContent()))
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .isEmpty(page.isEmpty())
                .totalPage(page.getTotalPages())
                .totalElements(page.getTotalElements()).build();
    }
}
