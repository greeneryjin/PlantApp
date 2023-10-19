package com.example.demo.account.dto.request.saveDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateDto {

    private String nickName;
    private String selfInfo;
    private String address;
}
