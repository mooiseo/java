package kr.co.samsung.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

import lombok.extern.slf4j.Slf4j;



@Slf4j
//@Component
//@Aspect
public class LoggerAspect {
    
    //@Around("execution(* com.hospital.his..service.*Impl.*(..)) or execution(* com.hospital.his..controller.*Controller.*(..)) or execution(* com.hospital.his..applicationService.*Impl.*(..))")
    public Object aroundLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = joinPoint.getSignature().getName();
        String type = joinPoint.getSignature().getDeclaringTypeName();
        
        log.debug("{}.{}() �떆�옉", type, name);
        
        Object returnValue = joinPoint.proceed();
        
        log.debug("{}.{}() �걹", type, name);
        
        return returnValue;
        
    }
}
