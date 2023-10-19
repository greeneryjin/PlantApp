package com.example.demo.s3.dto;

import lombok.Getter;

@Getter
public class ProfileDto {

    //올린 이미지
    private String uploadFile;

    //저장용 이미지
    private String storeMyPlantFile;

    private String url;

    private String fileName;

    public ProfileDto(String uploadFile, String storeMyPlantFile, String url, String fileName) {
        this.uploadFile = uploadFile;
        this.storeMyPlantFile = storeMyPlantFile;
        this.url = url;
        this.fileName = fileName;
    }
}
