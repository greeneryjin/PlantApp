package com.example.demo.mygarden.controller;

import com.example.demo.mygarden.entity.PlantWeather;
import com.example.demo.mygarden.service.PlantWeatherDataService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.FileInputStream;
import java.util.Iterator;

@ApiIgnore
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlantWeatherDataController {

    private final PlantWeatherDataService plantWeatherDataService;

    @GetMapping("/excel/plant")
    public void savePlant() {
        String fileName = "weatherPlantData.xls";

        try (FileInputStream fis = new FileInputStream(fileName)) {

            //엑셀 생성
            HSSFWorkbook workbook = new HSSFWorkbook(fis);

            //엑셀 1번 시트
            HSSFSheet sheet = workbook.getSheetAt(0);

            //엑셀 행
            Iterator<Row> rows = sheet.rowIterator();

            //1번 행 제외
            rows.next();

            String[] imsi = new String[12];

            while (rows.hasNext()) {
                HSSFRow row = (HSSFRow) rows.next();
                Iterator<Cell> cells = row.cellIterator();
                int i = 0;

                while (cells.hasNext()) {
                    HSSFCell cell = (HSSFCell) cells.next();
                    String tmp = cell.toString();

                    if (tmp.contains(".0")) {
                        int index = tmp.indexOf(".");
                        imsi[i] = tmp.substring(0, index);
                    } else {
                        imsi[i] = cell.toString();
                    }
                    i++;
                }
                PlantWeather plantWeather = new PlantWeather(imsi[0], imsi[1], imsi[2], imsi[3],
                        imsi[4], imsi[5], imsi[6], imsi[7], imsi[8], imsi[9] , imsi[10], imsi[11]);
                plantWeatherDataService.save(plantWeather);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/excel/plant/add")
    public void addPlantPic(){
        plantWeatherDataService.addPlantPic();
    }
}
