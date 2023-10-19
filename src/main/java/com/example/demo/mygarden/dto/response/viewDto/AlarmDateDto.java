package com.example.demo.mygarden.dto.response.viewDto;

import com.example.demo.mygarden.entity.MyPlant;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Setter
@Getter
public class AlarmDateDto {

    private Long id;

    private String url;
    private String nickName;
    private String uploadFiles;
    private LocalDate plusDays;
    private LocalDate standardWaterDate;
    private LocalDate minusDays;
    private long waterDDay;

    public AlarmDateDto(MyPlant myPlant, LocalDate now) {
        this.id = myPlant.getId();
        this.url = myPlant.getUrl();
        this.nickName = myPlant.getNickName();
        this.plusDays = myPlant.getPlusDays();
        this.standardWaterDate = myPlant.getStandardWaterDate();
        this.minusDays = myPlant.getMinusDays();
        Day(standardWaterDate, now);
    }

    //디 데이 계산
    private void Day(LocalDate standardWaterDate, LocalDate now) {
        long date = now.until(standardWaterDate, ChronoUnit.DAYS);
        setWaterDDay(date);
    }
}
