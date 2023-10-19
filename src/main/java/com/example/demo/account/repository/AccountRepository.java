package com.example.demo.account.repository;

import com.example.demo.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByNickName(String nickName);

    boolean existsAccountByNickName(String nickName);
    boolean existsAccountByEmail(String email);

    Account findBySnsId(String snsId);
    Account findByProfileUrl(String profileUrl);
}
