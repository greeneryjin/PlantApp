package com.example.demo.mygarden.dto.response.viewDto;

import com.example.demo.common.enums.HomePlace;
import com.example.demo.mygarden.entity.MyPlant;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Setter
@Getter
public class MyPlantDto {

    private String nickName;
    private String url;
    private String plantName;
    private String nameEn;
    private LocalDate createAdopt;
    private LocalDate createWater;
    private LocalDate createFertilizer;
    private HomePlace place;
    private Long adoptDay;

    public MyPlantDto(MyPlant myPlant, LocalDate now) {
        this.nickName = myPlant.getNickName();
        this.url = myPlant.getUrl();
        this.plantName = myPlant.getPlantName();
        this.nameEn = myPlant.getNameEn();
        this.createAdopt = myPlant.getCreateAdopt();
        this.createWater = myPlant.getCreateWater();
        this.createFertilizer = myPlant.getCreateFertilizer();
        this.place = myPlant.getPlace();
        Day(createAdopt, now);
    }

    //키운 날짜 계산
    private void Day(LocalDate createAdopt, LocalDate now) {
        long betweenDay = ChronoUnit.DAYS.between(createAdopt, now);
        setAdoptDay(betweenDay);
    }
}
