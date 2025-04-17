package org.test.sotfgen.service.classes;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.Exceptions.EmailNotFoundException;
import org.test.sotfgen.dto.EmailDto;
import org.test.sotfgen.entity.EmailEntity;
import org.test.sotfgen.repository.EmailRepository;
import org.test.sotfgen.service.interfaces.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;

    @Override
    public Page<EmailEntity> getEmails(EmailDto params, Pageable pageable) {
        return emailRepository.findAll(((root, query, criteriaBuilder) -> buildPredicate(params, root, criteriaBuilder)), pageable);
    }

    @Override
    public EmailEntity getEmailById(Integer id) {
        return emailRepository.findById(id).orElseThrow(() -> new EmailNotFoundException("Email with id " + id + " not found"));
    }

    private Predicate buildPredicate(EmailDto params, Root<EmailEntity> root, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        if (params.getFromEmail() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("fromEmail"), params.getFromEmail()));
        }
        if (params.getToEmail() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("toEmail"), params.getToEmail()));
        }
        if (params.getSubject() != null) {
            predicate = cb.and(predicate, cb.like(root.get("subject"), "%" + params.getSubject() + "%"));
        }
        if (params.getBody() != null) {
            predicate = cb.and(predicate, cb.like(root.get("body"), "%" + params.getBody() + "%"));
        }

        return predicate;
    }
}
