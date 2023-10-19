package com.example.demo.mygarden.service;

import com.example.demo.mygarden.entity.MyPlantPic;
import com.example.demo.mygarden.entity.PlantWeather;
import com.example.demo.mygarden.repository.MyPlantPicRepository;
import com.example.demo.mygarden.repository.PlantWeatherDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantWeatherDataService {
    private final PlantWeatherDataRepository plantWeatherDataRepository;
    private final MyPlantPicRepository myPlantPicRepository;

    public void save(PlantWeather plantWeather){
        plantWeatherDataRepository.save(plantWeather);
    }

    @Transactional
    public void addPlantPic() {
        List<MyPlantPic> all = myPlantPicRepository.findAll();
        List<PlantWeather> plantWeatherList = plantWeatherDataRepository.findAll();
        String s = "";

        String url = "https://grnr-s3.s3.ap-northeast-2.amazonaws.com/images/";

        for (int i = 0; i < all.size(); i++) {
            String myId = plantWeatherList.get(i).getPlantId();
            for (int j = 0; j < all.size(); j++) {
                String plantId = all.get(j).getPlantId();
                String plantPic = all.get(j).getPlantPic();
                if (myId.equals(plantId)) {
                    s = url + plantPic;
                }
            }
            plantWeatherList.get(i).setPlantPic(s);
        }
    }
}
