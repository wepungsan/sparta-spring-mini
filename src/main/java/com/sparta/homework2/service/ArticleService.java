package com.sparta.homework2.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.homework2.dto.ArticlePasswordRequestDto;
import com.sparta.homework2.dto.ArticleRequestDto;
import com.sparta.homework2.dto.ArticleResponseDto;
import com.sparta.homework2.dto.request.ContentRequestDto;
import com.sparta.homework2.dto.request.SingerRequestDto;
import com.sparta.homework2.dto.request.SongRequestDto;
import com.sparta.homework2.dto.request.TitleRequestDto;
import com.sparta.homework2.jwt.TokenProvider;
import com.sparta.homework2.model.Article;
import com.sparta.homework2.model.Member;
import com.sparta.homework2.repository.ArticleRepository;
import com.sparta.homework2.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final AmazonS3 amazonS3;
    private final AmazonS3Client amazonS3Client;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<ArticleResponseDto> getArticles() throws SQLException {
        List<ArticleResponseDto> articlesDto = articleRepository.findAll()
                .stream().map((article) -> {
                    String image = article.getImage();
                    String imgPath = amazonS3Client.getUrl(bucket, image).toString();
                    article.setImage(imgPath);
                    return article.toDto();
                }).collect(Collectors.toList());

        return articlesDto;
    }

    public ArticleResponseDto getArticle(Long id) throws SQLException {
        Article article = articleRepository.findById(id).orElse(null);
        String image = article.getImage();

        // ????????? ????????????
        String imgPath = amazonS3Client.getUrl(bucket, image).toString();

        // ????????? ????????? ?????? ??????
        article.setImage(imgPath);
        
        // ????????? DTO??? ??????
        // DTO??? ????????? ?????? ??????
        ArticleResponseDto articleDto = article.toDto();

        return articleDto;
    }

    @Transactional
    public Article createArticle(TitleRequestDto titleRequestDto, ContentRequestDto contentRequestDto, SongRequestDto songRequestDto
            , SingerRequestDto singerRequestDto, MultipartFile multipartFile) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long authId = Long.parseLong(auth.getName());

        Member member = memberRepository.findById(authId)
                .orElseThrow(() -> new RuntimeException("????????? ?????? ????????? ????????????."));

        String s3FileName = null;
        // String image = null;
        if(!multipartFile.isEmpty()) {
            s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(multipartFile.getInputStream().available());

            amazonS3.putObject(bucket,s3FileName,multipartFile.getInputStream(),objMeta);
            // image = amazonS3.getUrl(bucket,s3FileName).toString();
        }

        // ???????????? DTO ??? DB??? ????????? ?????? ?????????
        Article article = new Article(member.getUsername(), titleRequestDto, contentRequestDto, songRequestDto, singerRequestDto, s3FileName);

        articleRepository.save(article);

        return article;
    }

    public Long deleteArticle(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long authId = Long.parseLong(auth.getName());

        Member member = memberRepository.findById(authId)
                .orElseThrow(() -> new RuntimeException("????????? ?????? ????????? ????????????."));

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("?????? ?????? ???????????? ????????????."));

        if(!member.getUsername().equals(article.getAuthor())) {
            throw new RuntimeException("???????????? ????????? ??? ????????????.");
        }

        articleRepository.deleteById(id);
        return id;
    }

    @Transactional
    public Article updateArticle(Long id, TitleRequestDto titleRequestDto, ContentRequestDto contentRequestDto, SongRequestDto songRequestDto
            , SingerRequestDto singerRequestDto) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long authId = Long.parseLong(auth.getName());

        Member member = memberRepository.findById(authId)
                .orElseThrow(() -> new RuntimeException("????????? ?????? ????????? ????????????."));

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("?????? ?????? ???????????? ????????????."));

        if(!member.getUsername().equals(article.getAuthor())) {
           throw new RuntimeException("???????????? ????????? ??? ????????????.");
        }

        String image = article.getImage();

        // ????????? ????????????
        String imgPath = amazonS3Client.getUrl(bucket, image).toString();

        // ????????? ????????? ?????? ??????
        String s3FileName = imgPath;

        // ?????? ??????
        article.update(member.getUsername(), titleRequestDto, contentRequestDto, songRequestDto, singerRequestDto, s3FileName);

        articleRepository.save(article);

        return article;
    }
}
