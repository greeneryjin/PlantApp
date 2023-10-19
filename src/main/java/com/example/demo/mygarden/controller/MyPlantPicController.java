package com.example.demo.mygarden.controller;

import com.example.demo.mygarden.entity.MyPlantPic;
import com.example.demo.mygarden.service.MyPlantPicService;
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
public class MyPlantPicController {

    private final MyPlantPicService myPlantPicService;

    @GetMapping("/excel/pic")
    public void savePic() {
        String fileName = "plantPic.xls";

        try (FileInputStream fis = new FileInputStream(fileName)) {

            //엑셀 생성
            HSSFWorkbook workbook = new HSSFWorkbook(fis);

            //엑셀 1번 시트
            HSSFSheet sheet = workbook.getSheetAt(0);

            //엑셀 행
            Iterator<Row> rows = sheet.rowIterator();

            //1번 행 제외
            rows.next();

            String[] imsi = new String[2];

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
                    } else if (tmp.contains("|")) {
                        int index = tmp.indexOf("|");
                        imsi[i] = tmp.substring(0, index);
                    } else {
                        imsi[i] = cell.toString();
                    }
                    i++;
                }
                MyPlantPic myPlantPic = new MyPlantPic(imsi[0], imsi[1]);
                myPlantPicService.save(myPlantPic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
