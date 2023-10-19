package com.example.demo.account.dto.response.viewDto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountDto {

    private Long accountId;
    private String nickName;
    private String selfInfo;
    private String profileUrl;
    private int followerCount;
    private int followingCount;
    private int scrapCount;
    private int likeCount;
}
