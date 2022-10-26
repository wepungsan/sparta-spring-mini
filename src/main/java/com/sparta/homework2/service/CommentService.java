package com.sparta.homework2.service;

import com.sparta.homework2.dto.*;
import com.sparta.homework2.jwt.TokenProvider;
import com.sparta.homework2.model.Article;
import com.sparta.homework2.model.Comment;
import com.sparta.homework2.model.Member;
import com.sparta.homework2.repository.ArticleRepository;
import com.sparta.homework2.repository.CommentRepository;
import com.sparta.homework2.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public List<CommentResponseDto> getComments(Long id) throws SQLException {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("해당 글이 없습니다."));

        List<CommentResponseDto> comments = commentRepository.findAllByArticle(article)
                .orElseThrow(() -> new NullPointerException("해당 글이 없습니다."))
                .stream().map(comment -> comment.toDto()).collect(Collectors.toList());

        return comments;
    }

    public Comment createComment(Long id, CommentRequestDto requestDto) throws SQLException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long authId = Long.parseLong(auth.getName());

        Member member = memberRepository.findById(authId)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("해당 글이 없습니다."));

        Comment comment = new Comment(member.getUsername(), requestDto.getComment(), article);

        commentRepository.save(comment);

        return comment;
    }

    @Transactional
    public Comment updateComment(Long id, CommentUpdateRequestDto requestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long authId = Long.parseLong(auth.getName());

        Member member = memberRepository.findById(authId)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("해당 댓글이 존재하지 않습니다."));

        if(!member.getUsername().equals(comment.getName())) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }

        comment.setComment(requestDto.getComment());
        commentRepository.save(comment);

        return comment;
    }

    public Long deleteComment(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long authId = Long.parseLong(auth.getName());

        Member member = memberRepository.findById(authId)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("해당 댓글이 존재하지 않습니다."));

        if(!member.getUsername().equals(comment.getName())) {
            throw new RuntimeException("작성자만 삭제할 수 있습니다.");
        }

        commentRepository.deleteById(id);
        return id;
    }
}
