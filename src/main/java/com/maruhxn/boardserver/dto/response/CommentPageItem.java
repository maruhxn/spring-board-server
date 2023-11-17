package com.maruhxn.boardserver.dto.response;

import com.maruhxn.boardserver.dto.response.object.CommentItem;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class CommentPageItem {
    private List<CommentItem> comments;
    private Boolean isFirst;
    private Boolean isLast;
    private Boolean isEmpty;
    private int totalPage;
    private Long totalElements;

    public static CommentPageItem fromPage(Page<CommentItem> page) {
        System.out.println("page.getContent() = " + page.getContent());
        return CommentPageItem.builder()
                .comments(page.getContent())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .isEmpty(page.isEmpty())
                .totalPage(page.getTotalPages())
                .totalElements(page.getTotalElements()).build();
    }
}
