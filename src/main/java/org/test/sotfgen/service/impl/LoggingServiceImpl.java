package org.test.sotfgen.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.test.sotfgen.entity.HttpRequestEntity;
import org.test.sotfgen.repository.LoggingRepository;
import org.test.sotfgen.service.interfaces.LoggingService;
import org.test.sotfgen.utils.UserUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class LoggingServiceImpl implements LoggingService {

    private final LoggingRepository loggingRepository;
    private final UserUtil userUtil;

    @Value("${jwt.header}")
    private String jwtHeader;

    @Transactional
    @Override
    public void logRequest(HttpServletRequest req, HttpServletResponse res, long duration) {
        HttpRequestEntity log = new HttpRequestEntity();

        log.setMethod(req.getMethod());
        log.setUrl(req.getRequestURI());
        log.setQueryParams(req.getQueryString());
        log.setRequestBody(extractBody(req));
        log.setResponseStatus(res.getStatus());
        log.setDurationMillis(duration);
        log.setIp(req.getRemoteAddr());
        log.setUserAgent(req.getHeader("User-Agent"));
        log.setUsername(extractUsername(req));
        log.setRequestTime(new Date());

        loggingRepository.save(log);
    }

    private String extractUsername(HttpServletRequest req) {
        if(req.getHeader(jwtHeader) != null) {
            return userUtil.getActingPrincipal().getUsername();
        }

        return "Anonymous User";
    }

    private String extractBody(HttpServletRequest req) {
        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        } catch (IOException e) {
            // Handle exception
        }
        return body.toString();
    }
}