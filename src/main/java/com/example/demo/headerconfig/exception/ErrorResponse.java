package com.example.demo.headerconfig.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ErrorResponse { //상태 결과(예외) 응답

    //api 통신시간
    private LocalDateTime transactionTime;

    private String code;

    private String description;

    private String detail;

    public ErrorResponse(String code, String description, String detail) {
        this.transactionTime = LocalDateTime.now();
        this.code = code;
        this.description = description;
        this.detail = detail;
    }

    public ErrorResponse(String code, String description) {
        this.transactionTime = LocalDateTime.now();
        this.code = code;
        this.description = description;
    }
}
