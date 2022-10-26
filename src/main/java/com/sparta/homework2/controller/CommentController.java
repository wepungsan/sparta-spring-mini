package com.sparta.homework2.controller;

import com.sparta.homework2.dto.CommentRequestDto;
import com.sparta.homework2.dto.CommentUpdateRequestDto;
import com.sparta.homework2.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.sql.SQLException;

@RequiredArgsConstructor
@RestController // JSON으로 데이터를 주고받음을 선언합니다.
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/api/comments/{id}")
    public ResponseEntity<?> getComments(@PathVariable Long id) throws SQLException {
        try {
            return ResponseEntity.ok(commentService.getComments(id));
        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(404));
        }
    }

    @PostMapping("/api/comment/{id}")
    public ResponseEntity<?> createArticle(@PathVariable Long id, @RequestBody CommentRequestDto requestDto) throws SQLException {
        return ResponseEntity.ok(commentService.createComment(id, requestDto));
    }

    @PutMapping("/api/comment/{id}")
    public ResponseEntity<?> updateMemo(@PathVariable Long id, @RequestBody CommentUpdateRequestDto requestDto) {
        try {
            return ResponseEntity.ok(commentService.updateComment(id, requestDto));
        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(404));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(403));
        }
    }

    @DeleteMapping("/api/comment/{id}")
    public ResponseEntity<?> deleteMemo(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok(id);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(404));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(403));
        }
    }
}
