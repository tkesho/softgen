package org.test.sotfgen.minio;

import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

abstract class FileNameGeneration {
    public String generateFileName(MultipartFile file) {
        String extension = getExtension(file);
        return UUID.randomUUID().toString().replace("-", "") + "." + extension;
    }

    private String getExtension(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }
}