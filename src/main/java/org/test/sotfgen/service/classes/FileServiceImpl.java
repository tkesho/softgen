package org.test.sotfgen.service.classes;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.test.sotfgen.minio.MinioProps;
import org.test.sotfgen.minio.MinioService;
import org.test.sotfgen.repository.FileRepository;
import org.test.sotfgen.service.interfaces.FileService;

import java.io.InputStream;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final MinioService minioService;
    private final MinioProps minioProps;

    public FileServiceImpl(FileRepository fileRepository, @Qualifier("MinioServiceMinio") MinioService minioService, MinioProps minioProps) {
        this.fileRepository = fileRepository;
        this.minioService = minioService;
        this.minioProps = minioProps;
    }

    @Override
    public InputStream downloadFile(String objectKey) {
        return minioService.downloadFile(objectKey, minioProps.getBucket());
    }

    @Override
    public String uploadFile(MultipartFile file) {
        return minioService.uploadFile(file, minioProps.getBucket());
    }
}
