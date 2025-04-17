package org.test.sotfgen.service.classes;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.EmailDto;
import org.test.sotfgen.entity.EmailEntity;
import org.test.sotfgen.mapper.EmailMapper;
import org.test.sotfgen.repository.EmailRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    private final JavaMailSender emailSender;
    private final EmailRepository emailRepository;
    private final EmailMapper emailMapper;

    @Transactional
    public void sendEmail(
            String toEmail,
            String subject,
            String body
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("info@softgen.ge");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        EmailEntity email = emailMapper.emailDtoToEmailEntity(new EmailDto("info@softgen.ge", toEmail, subject, body));

        emailRepository.save(email);
        emailSender.send(message);

        log.info("Email sent to {} with subject: {}", toEmail, subject);
    }
}
