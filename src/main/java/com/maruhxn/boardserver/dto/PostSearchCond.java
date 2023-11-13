package com.maruhxn.boardserver.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotNull(message = "page 값을 입력해주세요.")
    @PositiveOrZero
    private Integer page;
}
