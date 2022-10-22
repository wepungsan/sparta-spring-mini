package com.sparta.homework2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.homework2.dto.ArticleRequestDto;
import com.sparta.homework2.dto.ArticleResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Article extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String content;

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    public Article(String title, String author, String password, String content) {
        this.title = title;
        this.author = author;
        this.password = password;
        this.content = content;
    }

    public Article(String username, ArticleRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.author = username;
        this.password = requestDto.getPassword();
        this.content = requestDto.getContent();
    }

    public void update(String username, ArticleRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.author = username;
        this.password = requestDto.getPassword();
        this.content = requestDto.getContent();
    }

    public ArticleResponseDto toDto() {
        int likseSize = this.likes.size();
        return new ArticleResponseDto(this.title, this.author, this.content, this.comments, likseSize);
    }
}
