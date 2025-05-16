package org.test.sotfgen.service.interfaces;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public interface FileService {
    InputStream downloadFile(String objectKey);

    String uploadFile(MultipartFile file);
}
