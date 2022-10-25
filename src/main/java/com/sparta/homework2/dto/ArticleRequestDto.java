package com.sparta.homework2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ArticleRequestDto {
    private String title;
    private String content;
    private String singer;
    private String song;

    public ArticleRequestDto(String title, String content, String singer, String song) {
        this.title = title;
        this.content = content;
        this.singer = singer;
        this.song = song;
    }
}
