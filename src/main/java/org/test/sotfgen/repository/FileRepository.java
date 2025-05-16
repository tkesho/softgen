package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.AttachmentEntity;

import java.util.Optional;

@Repository
public interface FileRepository extends BasicRepository<AttachmentEntity, Integer> {
    Optional<AttachmentEntity> findByObjectKey(String objectKey);
}
