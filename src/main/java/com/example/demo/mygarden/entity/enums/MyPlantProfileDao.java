package com.example.demo.mygarden.entity.enums;

import lombok.Getter;

@Getter
public class MyPlantProfileDao {

    //올린 이미지
    private String uploadFile;

    //저장용 이미지
    private String storeMyPlantFile;

    private String url;

    public MyPlantProfileDao(String uploadFile, String storeMyPlantFile, String url) {
        this.uploadFile = uploadFile;
        this.storeMyPlantFile = storeMyPlantFile;
        this.url = url;
    }
}
