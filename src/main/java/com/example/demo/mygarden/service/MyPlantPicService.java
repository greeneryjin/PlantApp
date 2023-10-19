package com.example.demo.mygarden.service;

import com.example.demo.mygarden.entity.MyPlantPic;
import com.example.demo.mygarden.repository.MyPlantPicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPlantPicService{

    private final MyPlantPicRepository myPlantPicRepository;

    public void save(MyPlantPic myPlantPic) {
        myPlantPicRepository.save(myPlantPic);
    }
}
