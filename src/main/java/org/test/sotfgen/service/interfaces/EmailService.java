package org.test.sotfgen.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.EmailDto;
import org.test.sotfgen.entity.EmailEntity;

@Service
public interface EmailService {
    Page<EmailEntity> getEmails(EmailDto params, Pageable pageable);

    EmailEntity getEmailById(Integer id);
}
