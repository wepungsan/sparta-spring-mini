package com.sparta.homework2.model;

import com.sparta.homework2.dto.ArticleResponseDto;
import com.sparta.homework2.dto.CommentResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class Comment extends Timestamped {

    // ID가 자동으로 생성 및 증가합니다.
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    public Comment(String name, String comment, Article article) {
        this.name = name;
        this.comment = comment;
        this.article = article;
    }

    public CommentResponseDto toDto() {
        return new CommentResponseDto(this.id, this.name, this.comment);
    }
}
