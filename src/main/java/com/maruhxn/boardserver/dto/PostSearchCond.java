package com.maruhxn.boardserver.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSearchCond {
    private String title;
    private String content;
    private String author;

    @Builder
    public PostSearchCond(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
