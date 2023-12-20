package com.maruhxn.boardserver.dto.request.comments;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateCommentRequest {
    @NotEmpty(message = "댓글 내용을 입력해주세요.")
    private String content;

    @Builder
    public CreateCommentRequest(String content) {
        this.content = content;
    }
}
