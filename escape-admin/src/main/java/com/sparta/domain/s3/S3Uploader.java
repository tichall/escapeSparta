package com.sparta.domain.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.sparta.global.exception.customException.S3Exception;
import com.sparta.global.exception.errorCode.S3ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static com.sparta.domain.s3.S3Util.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadStoreImage(MultipartFile file, Long storeId) {
        String imageDir = createStoreImageDir(storeId); // 파일 저장 경로 생성
        return uploadImage(file, imageDir);
    }

    public String uploadThemeImage(MultipartFile file, Long storeId, Long themeId) {
        String imageDir = createThemeImageDir(storeId, themeId);
        return uploadImage(file, imageDir);
    }

    private String uploadImage(MultipartFile file, String imageDir) {
        if(!doesFileExist(file)) {
            return null;
        }
        String extension = getValidateImageExtension(file.getOriginalFilename());
        
        String uploadFileName = imageDir + S3Util.createFileName(extension);
        return uploadFileToS3(file, uploadFileName);
    }

    /**
     * 실제 S3에 이미지 파일 업로드
     */
    private String uploadFileToS3(MultipartFile file, String uploadFileName) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, uploadFileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            log.error("이미지 업로드 중 오류가 발생했습니다.", e);
            throw new S3Exception(S3ErrorCode.IMAGE_STREAM_ERROR);
        }
        return amazonS3Client.getUrl(bucket, uploadFileName).toString();
    }

    /**
     * DB에 저장된 이미지 링크로 S3의 단일 이미지 파일 삭제
     */
    public void deleteFileFromS3(String imagePath) {
        if(imagePath == null) {
            return;
        }
        String splitStr = ".com/";
        String fileName = imagePath.substring(imagePath.lastIndexOf(splitStr) + splitStr.length());

        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    /**
     * 특정 디렉토리 내의 모든 파일 삭제
     */
    public void deleteFilesFromS3(String imageDir) {
        ObjectListing objectListing = amazonS3Client.listObjects(bucket, imageDir);

        if (objectListing.getObjectSummaries().isEmpty()) {
            log.info("파일이 존재하지 않습니다.");
            return;
        }

        while(true) {
            for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
                amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, summary.getKey()));
                log.info("삭제 : " + summary.getKey());
            }

            if(objectListing.isTruncated()) {
                objectListing = amazonS3Client.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }
    }

}

