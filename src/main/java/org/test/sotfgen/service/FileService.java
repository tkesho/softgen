package org.test.sotfgen.service;

import org.springframework.stereotype.Service;
import org.test.sotfgen.entity.FileEntity;

@Service
public interface FileService {
    FileEntity getFileById(Integer id);
}
