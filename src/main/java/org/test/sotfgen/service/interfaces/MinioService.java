package org.test.sotfgen.service.interfaces;

import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public interface MinioService {

    String uploadFile(String fileName, InputStream inputStream, Integer contentLength, String contentType);

    byte[] downloadFile(String objectKey);
}