package com.example.demo.security.user;

import com.example.demo.account.entity.Account;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

//인증을 위한 객체 생성.
@Getter
public class AccountContext extends User {

    private Account account;

    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getSnsId(), account.getEmail(), authorities);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}
