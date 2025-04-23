package org.test.sotfgen.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.audit.PostHistoryEntity;
import org.test.sotfgen.dto.PostHistorySearchParams;

@Service
public interface PostHistoryService {

    Page<PostHistoryEntity> getAllPostHistory(PostHistorySearchParams params, Pageable of);
}
