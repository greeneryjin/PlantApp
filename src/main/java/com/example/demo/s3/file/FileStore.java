package com.example.demo.s3.file;

import com.example.demo.s3.dto.ProfileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileStore {
    public ProfileDto storeOneFile(MultipartFile multipartFile, String folderName) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        String url = "https://grnr-s3.s3.ap-northeast-2.amazonaws.com" + folderName + "/" + storeFileName;
        String fileName = storeFileName;
        ProfileDto profileDto =  new ProfileDto(originalFilename, storeFileName, url, fileName);
        return profileDto;
    }

    public List<ProfileDto> storeFile(List<MultipartFile> multipartFile, String folderName) throws IOException {
        List<ProfileDto> profileDtoList = new ArrayList<>();
        for (int i = 0; i < multipartFile.size(); i++) {
            String originalFilename = multipartFile.get(i).getOriginalFilename();
            String storeFileName = createStoreFileName(originalFilename);
            String url = "https://grnr-s3.s3.ap-northeast-2.amazonaws.com" + folderName + "/" + storeFileName;
            String fileName = storeFileName;
            ProfileDto profileDto =  new ProfileDto(originalFilename, storeFileName, url, fileName);
            profileDtoList.add(profileDto);
        }
        return profileDtoList;
    }

    private String createStoreFileName(String originalFilename) {
        // 서버에 저장하는 파일 명
        String uuid = UUID.randomUUID().toString();
        // 사진 포맷 추출
        String ext = extractExt(originalFilename);
        String storeFileName = uuid + "." + ext;
        return storeFileName;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
