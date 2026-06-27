package com.whereitgo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class WIGAspect {

    // Executes for all methods inside service package
    @Around("execution(* com.whereitgo.service.*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();

        String className = joinPoint.getTarget()
                .getClass()
                .getSimpleName();

        String methodName = joinPoint.getSignature()
                .getName();

        Object[] args = joinPoint.getArgs();

        log.info("METHOD STARTED -> {}.{}() | Arguments : {}",
                className,
                methodName,
                Arrays.toString(args));

        try {

            Object result = joinPoint.proceed();

            long executionTime =
                    System.currentTimeMillis() - startTime;

            log.info("METHOD COMPLETED -> {}.{}() | Execution Time : {} ms",
                    className,
                    methodName,
                    executionTime);

            return result;

        } catch (Exception ex) {

            log.error("METHOD FAILED -> {}.{}() | Exception : {}",
                    className,
                    methodName,
                    ex.getMessage());

            throw ex;
        }
    }
}