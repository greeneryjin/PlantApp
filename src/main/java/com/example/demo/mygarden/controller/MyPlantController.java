package com.example.demo.mygarden.controller;

import com.example.demo.account.entity.Account;
import com.example.demo.headerconfig.Header;
import com.example.demo.mygarden.dto.request.saveDto.MyPlantSaveDto;
import com.example.demo.mygarden.dto.response.viewDto.AlarmDateDto;
import com.example.demo.mygarden.dto.response.viewDto.MyPlantDto;
import com.example.demo.mygarden.dto.response.viewDto.PlantPicDto;
import com.example.demo.mygarden.entity.PlantWeather;
import com.example.demo.mygarden.service.MyPlantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Api(tags = "마이가든")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyPlantController {

    private final MyPlantService myPlantService;

    /**
     * 1. 나의 식물 저장
     * 2. 나의 식물 전체 조회
     * 3. 나의 식물 저장할 때 필요한 식물 종 & 사진 조회
     * 4. 나의 식물 닉네임 수정(식물의 id)
     * 5. 물주기 일정 갱신(식물의 id)
     * 6. 모든 물 일정 확인
     * */
    @ApiOperation(value = "마이가든 저장")
    @PostMapping(value = "/user/myPlant/save")
    public Header<Long> saveMyPlant(@ApiParam(value = "닉네임, 식물 명, 식물 사진 url,  식물 영 명, 입양 날짜, 물 날짜, 비료 날짜, 키우는 장소", required = true)
                                    @Validated @RequestBody MyPlantSaveDto myPlantSaveDto) throws IOException {
        //사용자 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        Long myPlant = myPlantService.saveMyPlant(myPlantSaveDto, account);
        return Header.OK(myPlant);
    }

    @ApiOperation(value = "마이가든 전체 조회")
    @GetMapping("/user/myPlant/view")
    public Header<Page<MyPlantDto>> viewMyPlantList(@PageableDefault(size = 12) Pageable pageable) {
        //사용자 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        Page<MyPlantDto> myPlantList = myPlantService.findAllMyPlantList(pageable, account);
        return Header.OK(myPlantList);
    }

    @ApiOperation(value = "식물 사전에서 식물이름 조회")
    @GetMapping("/user/myPlantName/view")
    public Header<Page<PlantPicDto>> viewPlantWeather(@PageableDefault(size = 12) Pageable pageable,
                                                      @ApiParam(value = "식물 이름", required = true, example = "몬스테라")
                                                      @RequestParam(value = "plantName") String plantName) {
        Page<PlantWeather> plantWeatherPage = myPlantService.findAll(pageable, plantName);
        Page<PlantPicDto> plantWeatherDtos = plantWeatherPage.map(p -> new PlantPicDto(p));
        return Header.OK(plantWeatherDtos);
    }

    @ApiOperation(value = "마이가든 식물 닉네임 수정")
    @PutMapping("/user/myPlant/update/{id}")
    public Header<String> updatePlant(
                                      @ApiParam(value = "마이가든 id", required = true, example = "1")
                                      @PathVariable(value = "id") Long id,
                                      @ApiParam(value = "닉네임", required = true, example = "무몬이")
                                      @RequestParam(value = "name") String name) {
        myPlantService.updateNicknameMyPlant(id, name);
        return Header.OK("이름 수정이 완료되었습니다.");
    }

    @ApiOperation(value = "키우는 식물에 물줬으면 일정 업데이트")
    @PutMapping("/user/update/alarm/{id}")
    public Header<String> updateAlarm(@PathVariable(value = "id") Long id) {
        int i = myPlantService.updateAlarm(id);
        if (i == 1) {
            return Header.OK("일정보다 빨리 물을 주셨습니다.");
        } else if (i == 2) {
            return Header.OK("일정기간에 물을 주셨습니다.");
        } else {
            return Header.OK("일정보다 늦게 물을 주셨습니다.");
        }
    }

    @ApiOperation(value = "키우는 식물 물 알람 확인")
    @GetMapping("/user/view/alarm")
    public Header<Page<AlarmDateDto>> viewDataAlarm(@PageableDefault(size = 12) Pageable pageable) {

        //사용자 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        Page<AlarmDateDto> alarmDateDto = myPlantService.viewAlarmDate(pageable, account);
        return Header.OK(alarmDateDto);
    }

    /**
     * 물주기 일정 시작 전날 푸시알람
     */
    @ApiOperation(value = "물주기 일정 시작 전날 푸시 알람", notes = "푸시알람 기능 넣어야함")
    @GetMapping("/user/view/startWater/alarm")
    public Header<Map<String, String>> startWater() {
        //사용자 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        Map<String, String> map = myPlantService.alarmMinus(account);
        return Header.OK(map);
    }
}
