package org.test.sotfgen.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.test.sotfgen.service.interfaces.LoggingService;

import java.io.IOException;

public class RequestLoggingFilter extends OncePerRequestFilter {

    private final LoggingService loggingService;

    public RequestLoggingFilter(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(request, response);

        long start = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - start;

            loggingService.logRequest(wrappedRequest, wrappedResponse, duration);

            wrappedResponse.copyBodyToResponse();
        }
    }
}