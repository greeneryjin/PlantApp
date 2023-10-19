package com.example.demo.mygarden.dto.response.viewDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmWeatherDtoList {

    /**
     * 단기
     * */
    //강수형태 (pty)
    private String precipitationType;

    //강수확률 리스트 (pop)
    private String precipitationPer;

    //1시간 걍수량 리스트 (pcp)
    private String precipitation;

    //1시간 기온 리스트 (tmp)
    private String temperatureHour;

    //습도 REH
    private String humid;

    //하늘 상태
    private String skyType;

    //시간
    private String time;

    //날씨 사진
    private String weatherPic;
}
