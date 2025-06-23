package com.fast_campus_12.not_found.shop.global.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(public * com.fast_campus_12.not_found.shop..controller..*(..))")
    public void controllerMethods() {}

    @Pointcut("execution(public * com.fast_campus_12.not_found.shop..service..*(..))")
    public void serviceMethods() {}

    @Pointcut("execution(public * com.fast_campus_12.not_found.shop..mapper..*(..))")
    public void mapperMethods() {}

    @Around("controllerMethods()")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("[CONTROLLER] Start: {} args={}", joinPoint.getSignature(), joinPoint.getArgs());
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        log.info("[CONTROLLER] End: {} ({}ms)", joinPoint.getSignature(), duration);
        return result;
    }

    @Around("serviceMethods()")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("[SERVICE] Start: {} args={}", joinPoint.getSignature(), joinPoint.getArgs());
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        log.info("[SERVICE] End: {} ({}ms)", joinPoint.getSignature(), duration);
        return result;
    }

    @Around("mapperMethods()")
    public Object logMapper(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.debug("[MAPPER] Start: {} args={}", joinPoint.getSignature(), joinPoint.getArgs());
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        log.debug("[MAPPER] End: {} ({}ms)", joinPoint.getSignature(), duration);
        return result;
    }
}
