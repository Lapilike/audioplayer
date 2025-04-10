package by.lapil.audioplayer.utils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Before("execution(* by.lapil.audioplayer..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("[BEFORE] Вызов метода: {} с аргументами {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(value = "execution(* by.lapil.audioplayer..*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("[AFTER] Метод: {} выполнен. Возвращено: {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "execution(* by.lapil.audioplayer..*(..))", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        log.error("[ERROR] Метод: {} вызвал исключение: {}",
                joinPoint.getSignature(), exception.getMessage());
    }
}