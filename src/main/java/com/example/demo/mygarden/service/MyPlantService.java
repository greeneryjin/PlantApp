package com.example.demo.mygarden.service;

import com.example.demo.account.entity.Account;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.common.enums.HomePlace;
import com.example.demo.mygarden.dto.request.saveDto.MyPlantSaveDto;
import com.example.demo.mygarden.dto.response.viewDto.AlarmDateDto;
import com.example.demo.mygarden.dto.response.viewDto.MyPlantDto;
import com.example.demo.mygarden.entity.MyPlant;
import com.example.demo.mygarden.entity.PlantWeather;
import com.example.demo.mygarden.repository.MyPlantRepository;
import com.example.demo.mygarden.repository.PlantWeatherDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPlantService {

    private final MyPlantRepository myPlantRepository;

    private final PlantWeatherDataRepository plantWeatherDataRepository;

    private final AccountRepository accountRepository;

    //현재 시간
    LocalDate now = LocalDate.now();

    //나의 식물 저장
    @Transactional
    public Long saveMyPlant(MyPlantSaveDto myPlantSaveDto, Account account) throws IOException{

        //식물에 유저 저장
        MyPlant myPlant = myPlantSaveDto.entity();

        //식물 이름 찾기
        PlantWeather plantWeather = plantWeatherDataRepository.findByPlantName(myPlantSaveDto.getPlantName()).orElseThrow(NullPointerException::new);
        myPlant.addPlantName(plantWeather.getPlantName());
        myPlant.addPlantWeather(plantWeather);

        //물 주기 계산
        createWater(myPlantSaveDto.getCreateWater(), plantWeather, myPlant);
        String place = myPlantSaveDto.getPlace();

        switch (place) {
            case "원룸":
                myPlant.savePlace(HomePlace.ONE_ROOM);
                break;
            case "거실":
                myPlant.savePlace(HomePlace.LIVING_ROOM);
                break;
            case "침실":
                myPlant.savePlace(HomePlace.BEDROOM);
                break;
            case "주방":
                myPlant.savePlace(HomePlace.KITCHEN);
                break;
            case "베란다&발코니":
                myPlant.savePlace(HomePlace.VERANDA_BALCONY);
                break;
            case "사무실":
                myPlant.savePlace(HomePlace.OFFICE);
                break;
            case "가게":
                myPlant.savePlace(HomePlace.STORE);
                break;
            default:
                myPlant.savePlace(HomePlace.OUTDOOR);
                break;
        }
        myPlant.addAccount(account);
        myPlantRepository.save(myPlant);
        return myPlant.getId();
    }

    //식물 닉네임 변경
    @Transactional
    public void updateNicknameMyPlant(Long id, String name) {
        Optional<MyPlant> myPlant = myPlantRepository.findById(id);
        myPlant.get().updateNick(name);
    }

    //물 주기 갱신
    @Transactional
    public int updateAlarm(Long id) {

        //저장된 식물 꺼내기
        MyPlant myPlant = myPlantRepository.findById(id).orElseThrow(RuntimeException::new);

        //식물 날씨 데이터 꺼내기
        Long plantId = myPlant.getPlantWeather().getId();
        PlantWeather plantWeather = plantWeatherDataRepository.findById(plantId).orElseThrow(RuntimeException::new);

        //식물의 물 주기 일정 비교
        LocalDate minusDays = myPlant.getMinusDays();
        LocalDate standardDate = myPlant.getStandardWaterDate();
        LocalDate plusDays = myPlant.getPlusDays();

        //현재 시간이 일정보다 과거면 경고 및 저장
        if (now.isBefore(minusDays)) {
            //물 주기 계산
            createWater(now, plantWeather, myPlant);
            return 1;
            //일정에서 하나라도 포함되면 저장
        } else if(now.equals(minusDays) || now.equals(standardDate) || now.equals(plusDays)){
            createWater(now, plantWeather, myPlant);
            return 2;
            //현재 시간이 일정보다 미래면 경고 및 저장
        } else {
            createWater(now, plantWeather, myPlant);
            return 3;
        }
    }

    //물 주기 조회
    @Transactional(readOnly = true)
    public Page<AlarmDateDto> viewAlarmDate(Pageable pageable, Account account){
        //저장된 식물 꺼내기
        Page<MyPlant> plantPage = myPlantRepository.findAllByAccountId(pageable, account.getId()).orElseThrow(RuntimeException::new);
        Page<AlarmDateDto> alarmDateDto = plantPage.map(myPlant -> new AlarmDateDto(myPlant, now));
        return alarmDateDto;
    }

    //식물 종 & 사진
    public Page<PlantWeather> findAll(Pageable pageable, String plantName) {
        Page<PlantWeather> plantWeathers = plantWeatherDataRepository.findAllByPlantName(pageable, plantName);
        return plantWeathers;
    }

    //나의 식물 전체 조회
    @Transactional(readOnly = true)
    public Page<MyPlantDto> findAllMyPlantList(Pageable pageable, Account account) {
        LocalDate now = LocalDate.now();
        Page<MyPlant> myPlants = myPlantRepository.findAllByAccountId(pageable, account.getId()).orElseThrow(RuntimeException::new);
        Page<MyPlantDto> myPlantDtoPage = myPlants.map(myPlant -> new MyPlantDto(myPlant, now));
        return myPlantDtoPage;
    }

    //물주기 일정 전 날 물주기 푸시 알람
    public Map<String, String> alarmMinus(Account account) {

        Map<String, String> alarmMap = new HashMap<>();

        //저장된 식물 꺼내기
        List<MyPlant> myPlants = myPlantRepository.findAllByAccountId(account.getId()).orElseThrow(RuntimeException::new);

        for (int i = 0; i < myPlants.size(); i++) {
            LocalDate minusDays = myPlants.get(i).getMinusDays().minusDays(1);
            String nickName = myPlants.get(i).getNickName();

            //일정주기
            if(now.isEqual(minusDays)){
                alarmMap.put(nickName,"물주기 하루 전 날입니다.");
            }
        }
        return alarmMap;
    }

    //식물 물 주기 일정 생성
    public void createWater(LocalDate day, PlantWeather plantName, MyPlant myPlant) {  //220531

        //식물 계절별 물 주기 값 리턴
        int waterPeriod = findWaterSeason(day, plantName); //7

        //기준 날짜 만들기 = 처음 물준 날 + waterPeriod
        LocalDate standardDay = day.plusDays(waterPeriod); //220501 + 7 = 220508

        //기준 날짜 +-일 일정 생성
        int period = createPeriod(waterPeriod);

        LocalDate minusDays = standardDay.minusDays(period);
        LocalDate plusDays = standardDay.plusDays(period);

        //주기 날짜 저장
        myPlant.saveStandardWater(minusDays, standardDay, plusDays);
    }

    //식물 사계절
    public int findWaterSeason(LocalDate weatherDate, PlantWeather plantWeather) {

        //현재 계절 추출
        int month = weatherDate.getMonthValue();
        switch (month){
            //봄 2~4
            case 2: case 3: case 4:
                int spring =  Integer.parseInt(plantWeather.getSpringWatering());
                return spring;
            //여름 5~7
            case 5: case 6: case 7:
                //중기 강수 확률 & 평균 온도 확인 후, 물주기 일정 변경
                int summer =  Integer.parseInt(plantWeather.getSummerWatering());
                return summer;
            //가을 8~10
            case 8: case 9: case 10:
                int autumn = Integer.parseInt(plantWeather.getAutumnWatering());
                return autumn;
            //겨울
            default:
                int winter = Integer.parseInt(plantWeather.getWinterWatering());
                return winter;
        }
    }

    //기준 날짜에 해당 계절 주기의 만들기.
    private int createPeriod(int waterPeriod) {
        switch (waterPeriod){
            case 3: case 5:
                return 1;
            default:
                return 2;
        }
    }
}
