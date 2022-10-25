package com.sparta.homework2;

import com.sparta.homework2.model.*;
import com.sparta.homework2.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Homework2Application {
	public static void main(String[] args) {
		SpringApplication.run(Homework2Application.class, args);
	}

	@Bean
	public CommandLineRunner demo(ArticleRepository articleRepository, CommentRepository commentRepository,
								  MemberRepository memberRepository, LikeRepository likeRepository,
								  CategoryRepository categoryRepository) {
		return (args) -> {
			memberRepository.save(new Member("wepungsan", "mary", "1234", Authority.ROLE_USER));

			Member member1 = memberRepository.findById(1L)
							.orElseThrow(() -> new NullPointerException("해당 유저가 존재하지 않습니다."));

			articleRepository.save(new Article("제목01", "wepungsan", "내용01", "이선희", "노래01"));
			articleRepository.save(new Article("제목02", "wepungsan", "내용02", "이선희", "노래01"));
			articleRepository.save(new Article("제목03", "wepungsan", "내용03", "이선희", "노래01"));

			System.out.println("데이터 인쇄");
			List<Article> articleList = articleRepository.findAll();
			for (int i=0; i<articleList.size(); i++) {
				Article article = articleList.get(i);
				System.out.println(article.getId());
				System.out.println(article.getTitle());
				System.out.println(article.getAuthor());
				System.out.println(article.getContent());
				System.out.println(article.getSinger());
				System.out.println(article.getSong());
			}

			Article article1 = articleRepository.findById(1L)
					.orElseThrow(() -> new NullPointerException("해당 글이 존재하지 않습니다."));
			commentRepository.save(new Comment("wepungsan", "1글 1코멘트", article1));
			commentRepository.save(new Comment("wepungsan", "1글 2코멘트", article1));
			commentRepository.save(new Comment("wepungsan", "1글 3코멘트", article1));

			System.out.println("데이터 인쇄");
			List<Comment> commentList = commentRepository.findAll();
			for (int i=0; i<commentList.size(); i++) {
				Comment comment = commentList.get(i);
				System.out.println(comment.getId());
				System.out.println(comment.getName());
				System.out.println(comment.getComment());
			}

			likeRepository.save(new Like(member1, article1));
		};
	}
}
