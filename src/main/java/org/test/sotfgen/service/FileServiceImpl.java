package org.test.sotfgen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.test.sotfgen.Exceptions.file.FileNotFoundException;
import org.test.sotfgen.entity.FileEntity;
import org.test.sotfgen.repository.FileRepository;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public FileEntity getFileById(Integer id) {
        return fileRepository.findById(id).orElseThrow(() -> new FileNotFoundException("file with id + " + id + " not found"));
    }
}
