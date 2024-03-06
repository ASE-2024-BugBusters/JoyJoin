package com.joyjoin.eventservice.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private long start;


    @Before("execution(* com.joyjoin.eventservice.service..*.*(..))")
    public void logBeforeServiceMethodExecution() {
        start = System.currentTimeMillis();
    }

    @After("execution(* com.joyjoin.eventservice.service..*.*(..))")
    public void logAfterServiceMethodExecution(JoinPoint joinPoint) {
        long end = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toString();
        logger.info("Method: {}, Duration: {}ms", methodName, end-start);
    }
}