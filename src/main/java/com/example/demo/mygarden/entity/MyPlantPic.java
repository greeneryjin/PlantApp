package com.example.demo.mygarden.entity;

import com.example.demo.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class MyPlantPic extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plantPic_id")
    private Long id;

    @Column(length = 500)
    private String plantPic;

    @Column(length = 100)
    private String plantId;

    public MyPlantPic(String plantId, String plantPic) {
        this.plantPic = plantPic;
        this.plantId = plantId;
    }
}
