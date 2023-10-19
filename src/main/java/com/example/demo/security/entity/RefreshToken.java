package com.example.demo.security.entity;

import com.example.demo.account.entity.Account;

import com.example.demo.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refreshToken_id")
    private Long id;
    private String refreshToken;
    private String snsId;

    private Boolean reloadToken; //삭제

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public RefreshToken(String refreshToken, String snsId, Account account) {
        this.refreshToken = refreshToken;
        this.snsId = snsId;
        this.account = account;
    }
}
