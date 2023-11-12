package com.maruhxn.boardserver.dto.request.comments;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateCommentRequest {
    @NotEmpty
    private String content;
}
