package com.maruhxn.boardserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchCond {
    private String title;
    private String content;
    private String author;
    private Integer page;
}
