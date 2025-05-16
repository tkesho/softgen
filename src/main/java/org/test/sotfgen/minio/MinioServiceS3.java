package org.test.sotfgen.minio;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.test.sotfgen.exceptions.FileUploadException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service("MinioServiceS3")
@RequiredArgsConstructor
public class MinioServiceS3 extends FileNameGeneration implements MinioService {

    private final S3Client s3Client;

    @Override
    public String uploadFile(MultipartFile file, String bucketName) {

        if (file.isEmpty() || file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new FileUploadException("File must have name");
        }

        String fileName = generateFileName(file);
        InputStream inputStream;

        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            throw new FileUploadException("File upload error");
        }

        try {
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build(),
                RequestBody.fromInputStream(inputStream, file.getSize()));
        } catch (Exception e) {
            throw new FileUploadException("File upload error");
        }

        return fileName;
    }

    public InputStream downloadFile(String objectKey, String bucketName) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        try {
            return s3Client.getObject(getRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }
}
