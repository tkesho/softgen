package org.test.sotfgen.service.classes;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.test.sotfgen.entity.FileEntity;
import org.test.sotfgen.exceptions.FileNotFoundException;
import org.test.sotfgen.repository.FileRepository;
import org.test.sotfgen.service.interfaces.FileService;
import org.test.sotfgen.service.interfaces.MinioService;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final MinioService minioService;

    public FileServiceImpl(FileRepository fileRepository, @Qualifier("MinioServiceS3") MinioService minioService) {
        this.fileRepository = fileRepository;
        this.minioService = minioService;
    }

    @Override
    public byte[] downloadFile(String objectKey) {
        FileEntity fileToDownload = fileRepository.findByObjectKey(objectKey)
                .orElseThrow(() -> new FileNotFoundException("File with name \"" + objectKey + "\" not found"));

        return minioService.downloadFile(objectKey);
    }

    @Override
    public String uploadFile(String key, MultipartFile file) {
        try {
            minioService.uploadFile(
                    key,
                    file.getInputStream(),
                    (int) file.getSize(),
                    file.getContentType()
            );

            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(key);
            fileEntity.setSize(file.getSize());
            fileEntity.setContentType(file.getContentType());
            fileEntity.setObjectKey(key);

            fileRepository.save(fileEntity);

            return key;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}
