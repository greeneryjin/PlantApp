package com.example.demo.headerconfig.exception;

import lombok.Getter;

public enum ErrorCode {

    NOT_NULL("ERROR_CODE_0001","필수값이 누락되었습니다")
    ,NOT_BLANK("ERROR_CODE_0002", "필수값이 공란입니다.")
    ,NOT_FOUND("ERROR_CODE_0003", "값을 찾을 수 없습니다.")
    ;

    @Getter
    private String code;

    @Getter
    private String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
