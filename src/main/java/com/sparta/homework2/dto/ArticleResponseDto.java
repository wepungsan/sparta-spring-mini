package com.sparta.homework2.dto;

import com.sparta.homework2.model.Comment;
import com.sparta.homework2.model.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleResponseDto {
    private Long id;
    private String title;
    private String author;
    private String content;
    private String singer;
    private String song;
    private String image;
    private List<Comment> comments;
    private int likesSize;

    public ArticleResponseDto(Long id, String title, String author, String content, List<Comment> comments,
                              int likesSize, String image, String singer, String song) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.comments = comments;
        this.likesSize = likesSize;
        this.image = image;
        this.singer = singer;
        this.song = song;
    }
}
