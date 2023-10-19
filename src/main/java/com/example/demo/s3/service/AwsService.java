package com.example.demo.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.s3.dto.ProfileDto;
import com.example.demo.s3.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EmptyFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Component
public class AwsService {

    @Value("${aws.s3.bucket}")
    public String bucketName; //S3 버킷 경로

    private final AmazonS3 s3Client;

    private final FileStore fileStore;

    public ProfileDto uploadOneProfile(MultipartFile file, String folderName) throws IOException {

        validateFileExists(file);
        ProfileDto attachFile = fileStore.storeOneFile(file, folderName);
        File uploadFile = convertMultipartFileToFile(file)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        //S3 업로드
        s3Client.putObject(new PutObjectRequest(
                bucketName + folderName, attachFile.getStoreMyPlantFile(),
                uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));

        //생성된 로컬 파일 삭제
        removeNewFile(uploadFile);
        return attachFile;
    }

    public List<ProfileDto> uploadProfile(List<MultipartFile> file, String folderName) throws IOException {
        List<ProfileDto> attachFile = fileStore.storeFile(file, folderName);
        for (int i = 0; i < attachFile.size(); i++) {
            File uploadFile = convertMultipartFileToFile(file.get(i))
                    .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

            //S3 업로드
            s3Client.putObject(new PutObjectRequest(
                    bucketName + folderName, attachFile.get(i).getStoreMyPlantFile(),
                    uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));

            //생성된 로컬 파일 삭제
            removeNewFile(uploadFile);
        }
        return attachFile;
    }

    public void deleteProfile(String folderName, String fileName) throws IOException {
        s3Client.deleteObject(bucketName + folderName, fileName);
    }

    // convert multipart file to file method
    private Optional<File> convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        if(convertedFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertedFile);
        }

        return Optional.empty();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private void validateFileExists(MultipartFile multipartFile) {
        log.info("사진 업로드 사이즈 진입");
        if (multipartFile.isEmpty()) {
            throw new EmptyFileException();
        }
    }
}