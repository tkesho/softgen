package org.test.sotfgen.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.test.sotfgen.exceptions.FileUploadException;

import java.io.InputStream;

@Service("MinioServiceMinio")
@RequiredArgsConstructor
public class MinioServiceMinio extends FileNameGeneration implements MinioService {

    private final MinioClient minioClient;

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
            minioClient.putObject(PutObjectArgs.builder()
                    .stream(inputStream, inputStream.available(), -1)
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            throw new FileUploadException("File upload error");
        }

        return fileName;
    }

    public InputStream downloadFile(String objectKey, String bucketName) {
        try {
            var request = GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .build();

            InputStream returnVal = minioClient.getObject(request);

        } catch (Exception e) {
            throw new RuntimeException("Error while downloading file from Minio", e);
        }

        return null;
    }
}