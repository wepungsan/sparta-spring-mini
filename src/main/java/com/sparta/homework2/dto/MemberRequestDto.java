package com.sparta.homework2.dto;

import com.sparta.homework2.model.Authority;
import com.sparta.homework2.model.Member;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {
    private String username;
    private String nickname;
    private String password;
    private String passwordCheck;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .username(username)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .authority(Authority.ROLE_USER)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }

    static public UsernamePasswordAuthenticationToken toAuthenticationByUsernameAndPassword(String username, String password) {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
