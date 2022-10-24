package com.sparta.homework2.controller;

import com.sparta.homework2.dto.ArticlePasswordRequestDto;
import com.sparta.homework2.dto.ArticleRequestDto;
import com.sparta.homework2.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@RequiredArgsConstructor
@RestController // JSON으로 데이터를 주고받음을 선언합니다.
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/api/articles")
    public ResponseEntity<?> getArticles() throws SQLException {
        return ResponseEntity.ok(articleService.getArticles());
    }

    @GetMapping("/api/article/{id}")
    public ResponseEntity<?> getArticle(@PathVariable Long id) throws SQLException {
        return ResponseEntity.ok(articleService.getArticle(id));
    }

    @PostMapping(value = "/api/article", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createArticle(@RequestPart ArticleRequestDto requestDto, @RequestPart MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(articleService.createArticle(requestDto, multipartFile));
    }

    @PostMapping("/api/article/{id}")
    public ResponseEntity<?> checkPassword(@PathVariable Long id, @RequestBody ArticlePasswordRequestDto requestDto) throws SQLException {
        return ResponseEntity.ok(articleService.checkPassword(id, requestDto));
    }

    @DeleteMapping("/api/article/{id}")
    public ResponseEntity<?> deleteMemo(@PathVariable Long id) {
        try {
            articleService.deleteArticle(id);
            return ResponseEntity.ok(id);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(404));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(403));
        }
    }

    @PutMapping("/api/article/{id}")
    public ResponseEntity<?> updateMemo(@PathVariable Long id, @RequestBody ArticleRequestDto requestDto) {
        try {
            return ResponseEntity.ok(articleService.update(id, requestDto));
        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(404));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(403));
        }
    }
}
