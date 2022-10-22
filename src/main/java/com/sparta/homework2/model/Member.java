package com.sparta.homework2.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@Table(name = "member")
@Entity
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String nickname;
    private String password;
    private Authority authority;

    @Builder
    public Member(String username, String nickname, String password, Authority authority) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.authority = authority;
    }
}
