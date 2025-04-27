package org.test.sotfgen.service.interfaces;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {
    byte[] downloadFile(String objectKey);

    String uploadFile(String key, MultipartFile file);
}
