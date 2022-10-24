package com.sparta.homework2.controller;

import com.sparta.homework2.service.S3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class FileUploadController {

    private final S3Upload s3Upload;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("images") MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(
                s3Upload.upload(multipartFile));
    }
}
