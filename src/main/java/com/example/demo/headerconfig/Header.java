package com.example.demo.headerconfig;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Header<T> { //상태 결과(정상) 응답

    //api 통신시간
    private LocalDateTime transactionTime;

    private T value;

    //api 통신 설명
    private String description;

    //api 결과
    private boolean result;

    // OK
    public static <T> Header<T> OK() {
        return (Header<T>) Header.builder()
                .result(true)
                .transactionTime(LocalDateTime.now())
                .build();
    }

    // DATA OK
    public static <T> Header<T> OK(T data) {
        return (Header<T>)
                Header.builder()
                .result(true)
                .transactionTime(LocalDateTime.now())
                .value(data)
                .build();
    }

    // DATA OK
    public static <T> Header<T> description(T data, String description) {
        return (Header<T>)Header.builder()
                .result(true)
                .transactionTime(LocalDateTime.now())
                .value(data)
                .description(description)
                .build();
    }

    public static <T> Header<T> OK(T data, String address) {
        return (Header<T>)Header.builder()
                .result(true)
                .transactionTime(LocalDateTime.now())
                .value(data)
                .description(address)
                .build();
    }
}