package com.example.demo.mygarden.entity;

import com.example.demo.account.entity.Account;
import com.example.demo.common.BaseTimeEntity;
import com.example.demo.common.enums.HomePlace;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class MyPlant extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "myPlant_id")
    private Long id;

    private String nickName;
    private String plantName;
    private String nameEn;
    private LocalDate createAdopt;
    private LocalDate createWater;
    private LocalDate createFertilizer;
    @Enumerated(value = EnumType.STRING)
    private HomePlace place;

    //물주기 날짜
    private LocalDate plusDays;
    private LocalDate standardWaterDate;
    private LocalDate minusDays;

    //식물 프로필 사진
    private String url;

    //유저(FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    //식물 날씨 정보(FK)
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "plantWeather_id")
    private PlantWeather plantWeather;

    //연관관계 메서드 - 유저
    public void addAccount(Account account) {
        if(this.account != null){ //기존에 이미 존재하면
            this.account.getPlantList().remove(this); //관계 삭제
        }
        this.account = account;
        if(account != null){
            account.getPlantList().add(this);
        }
    }

    @Builder
    public MyPlant(String nickName, String plantName, String nameEn, LocalDate createAdopt, LocalDate createWater,
                   LocalDate createFertilizer, String pictureUrl) {
        this.nickName = nickName;
        this.plantName = plantName;
        this.nameEn = nameEn;
        this.createAdopt = createAdopt;
        this.createWater = createWater;
        this.createFertilizer = createFertilizer;
        this.url = pictureUrl;
    }

    //장소 저장
    public void savePlace(HomePlace place){
        this.place = place;
    }

    //식물 날씨 저장
    public void addPlantWeather(PlantWeather plantWeather){
        this.plantWeather = plantWeather;
    }

    //식물 이름 저장
    public void addPlantName(String plantName) {
        savePlantName(plantName);
    }

    public void savePlantName(String plantName) {
        this.plantName = plantName;
    }

    //식물 닉네임 수정
    public void updateNick(String nickName) {
        updateNickName(nickName);
    }

    private void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    //주기 생성
    public void saveStandardWater(LocalDate minusDays, LocalDate standardDay, LocalDate plusDays) {
        this.minusDays = minusDays;
        this.standardWaterDate = standardDay;
        this.plusDays = plusDays;
    }
}
