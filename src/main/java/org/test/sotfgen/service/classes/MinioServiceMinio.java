package org.test.sotfgen.service.classes;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.test.sotfgen.service.interfaces.MinioService;

import java.io.InputStream;

@Service("MinioServiceMinio")
@RequiredArgsConstructor
public class MinioServiceMinio implements MinioService {

    private final MinioClient minioClient;
    @Value("${minio.bucket.name}")
    private String BUCKET_NAME;

    @Override
    public String uploadFile(String fileName, InputStream inputStream, Integer contentLength, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(fileName)
                            .stream(inputStream, contentLength, -1)
                            .contentType(contentType)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to Minio", e);
        }
        
        return "http://localhost:9000/" + BUCKET_NAME + "/" + fileName;
    }

    @Override
    public byte[] downloadFile(String objectKey) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectKey)
                            .build()
            ).readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error while downloading file from Minio", e);
        }
    }
}