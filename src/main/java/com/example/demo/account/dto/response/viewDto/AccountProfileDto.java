package com.example.demo.account.dto.response.viewDto;

import com.example.demo.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountProfileDto {

    private Long accountId;
    private String nickName;
    private String address;
    private String homePage;
    private String selfInfo;
    private String profileUrl;
    private String email;

    public static AccountProfileDto of(Account account) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(account, AccountProfileDto.class);
    }
}
