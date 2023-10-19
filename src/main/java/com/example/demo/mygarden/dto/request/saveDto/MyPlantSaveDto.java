package com.example.demo.mygarden.dto.request.saveDto;

import com.example.demo.mygarden.entity.MyPlant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyPlantSaveDto {

    @NonNull
    private String nickName;

    @NonNull
    private String plantName;

    @NonNull
    private String pictureUrl;

    @NonNull
    private String nameEn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
    private LocalDate createAdopt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
    private LocalDate createWater;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
    private LocalDate createFertilizer;

    @NotNull
    private String place;

    public MyPlant entity() {
        MyPlant myPlant = MyPlant.builder()
                .nickName(nickName)
                .plantName(plantName)
                .pictureUrl(pictureUrl)
                .nameEn(nameEn)
                .createAdopt(createAdopt)
                .createWater(createWater)
                .createFertilizer(createFertilizer)
                .build();
        return myPlant;
    }
}
