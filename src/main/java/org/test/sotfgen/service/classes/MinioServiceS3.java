package org.test.sotfgen.service.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.test.sotfgen.service.interfaces.MinioService;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

@Service("MinioServiceS3")
@RequiredArgsConstructor
public class MinioServiceS3 implements MinioService {

    private final S3Client s3Client;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Override
    public String uploadFile(String fileName, InputStream inputStream, Integer contentLength, String contentType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));

        return fileName;
    }

    public byte[] downloadFile(String objectKey) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        try (ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getRequest)) {
            return response.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }
}
