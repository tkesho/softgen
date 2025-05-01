package org.test.sotfgen.service.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface LoggingService {
    void logRequest(HttpServletRequest req, HttpServletResponse res, long duration);
}
