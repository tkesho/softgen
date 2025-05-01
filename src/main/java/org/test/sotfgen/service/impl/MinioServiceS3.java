package org.test.sotfgen.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.test.sotfgen.service.interfaces.MinioService;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioServiceS3 implements MinioService {

    private final S3Client s3Client;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Override
    public void uploadFile(String fileName, InputStream inputStream, Integer contentLength, String contentType) {
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType(contentType)
                        .build(),
                RequestBody.fromInputStream(inputStream, contentLength)
        );
    }

    @Override
    public byte[] downloadFile(String fileName) {
        return s3Client.getObjectAsBytes(GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build()).asByteArray();
    }
}
