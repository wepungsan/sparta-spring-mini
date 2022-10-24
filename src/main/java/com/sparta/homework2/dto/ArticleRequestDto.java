package com.sparta.homework2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ArticleRequestDto {
    private String title;
    private String content;

    public ArticleRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
