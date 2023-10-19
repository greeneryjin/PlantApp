package com.example.demo.mygarden.entity;

import com.example.demo.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class PlantWeather extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plantWeather_id")
    private Long id;

    @Column(length = 100)
    private String plantName;

    @Column(length = 100)
    private String nameEn;

    @Column(length = 100)
    private String plantId;

    //온도
    private String tempHigh;
    private String tempLow;

    //겨울철 최저온도
    private String winterMinLow;

    //최고 습도
    @Column(length = 30)
    private String humidHigh;

    //최저 습도
    @Column(length = 30)
    private String humidLow;

    //계절별 물
    private String springWatering;

    private String summerWatering;

    private String autumnWatering;

    private String winterWatering;

    private String plantPic;

    public PlantWeather(String plantId, String plantName, String tempLow, String tempHigh, String winterMinLow, String humidLow,
                        String humidHigh, String springWatering, String summerWatering, String autumnWatering, String winterWatering, String nameEn) {
        this.plantId = plantId;
        this.plantName = plantName;
        this.tempHigh = tempHigh;
        this.tempLow = tempLow;
        this.winterMinLow = winterMinLow;
        this.humidHigh = humidHigh;
        this.humidLow = humidLow;
        this.springWatering = springWatering;
        this.summerWatering = summerWatering;
        this.autumnWatering = autumnWatering;
        this.winterWatering = winterWatering;
        this.nameEn = nameEn;
    }
}
