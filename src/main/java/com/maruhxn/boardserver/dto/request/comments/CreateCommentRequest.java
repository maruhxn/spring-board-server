package com.maruhxn.boardserver.dto.request.comments;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateCommentRequest {
    @NotEmpty(message = "댓글 내용을 입력해주세요.")
    private String content;
}
