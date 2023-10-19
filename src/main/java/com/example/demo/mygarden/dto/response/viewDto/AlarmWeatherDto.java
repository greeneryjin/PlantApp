package com.example.demo.mygarden.dto.response.viewDto;

import com.example.demo.mygarden.entity.MyPlant;
import lombok.Getter;

@Getter
public class AlarmWeatherDto {

    private String nickName;
    private String weatherComment;

    public AlarmWeatherDto(MyPlant myPlant, String weatherComment) {
        this.nickName = myPlant.getNickName();
        this.weatherComment = weatherComment;
    }
}
