package com.store.productservice.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;

@Component
public class RequestResponseLoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", Instant.now().toEpochMilli());
        logger.info("Incoming Request: {} {} | Params: {} | TraceId: {}",
                request.getMethod(), request.getRequestURI(),
                request.getQueryString() == null ? "N/A" : request.getQueryString(),
                request.getHeader("traceId"));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (long) request.getAttribute("startTime");
        long duration = Instant.now().toEpochMilli() - startTime;
        logger.info("Completed Request: {} {} | Status: {} | Execution Time: {}ms | TraceId: {}",
                request.getMethod(), request.getRequestURI(),
                response.getStatus(), duration,
                request.getHeader("traceId"));
    }
}
