package com.example.demo.mygarden.dto.response.viewDto;

import com.example.demo.mygarden.entity.PlantWeather;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlantPicDto {

    private String plantName;
    private String nameEn;
    private String plantPic;

    public PlantPicDto(PlantWeather p) {
        this.plantName = p.getPlantName();
        this.nameEn = p.getNameEn();
        this.plantPic = p.getPlantPic();
    }
}
