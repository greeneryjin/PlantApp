package com.example.demo.headerconfig;

import lombok.*;


@NoArgsConstructor
@Getter
@Setter
public class Result { //토큰 응답메세지

    //성공 or 실패
    private String status;

    //메세지
    private String message;

    //토큰 데이터
    private String data;

    public Result(String success, String message, String data) {
        this.status = success;
        this.message = message;
        this.data = data;
    }

    public Result(String status, String message) {
        this.status = status;
        this.message = message;
    }
}

