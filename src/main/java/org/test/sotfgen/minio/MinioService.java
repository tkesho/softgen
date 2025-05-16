package org.test.sotfgen.minio;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public interface MinioService {

    String uploadFile(MultipartFile file, String bucketName);

    InputStream downloadFile(String objectKey, String bucketName);
}