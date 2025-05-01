package org.test.sotfgen.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.test.sotfgen.exceptions.FileNotFoundException;
import org.test.sotfgen.entity.FileEntity;
import org.test.sotfgen.repository.FileRepository;
import org.test.sotfgen.service.interfaces.FileService;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public FileEntity getFileById(Integer id) {
        return fileRepository.findById(id).orElseThrow(() -> new FileNotFoundException("file with id + " + id + " not found"));
    }
}
