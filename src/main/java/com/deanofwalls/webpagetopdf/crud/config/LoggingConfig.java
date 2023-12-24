package com.deanofwalls.webpagetopdf.crud.config;

import com.deanofwalls.webpagetopdf.crud.config.utils.Loggable;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class LoggingConfig implements Loggable {

    //AOP expression for which methods shall be intercepted
    @Around("execution(* com.deanofwalls.webpagetopdf   ..*(..)))")
    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) {
        final MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        final String className = methodSignature.getDeclaringType().getSimpleName();
        final String methodName = methodSignature.getName();
        final String arguments = Arrays.toString(proceedingJoinPoint.getArgs());
        final StopWatch stopWatch = new StopWatch();
        Object result;

        //Measure method execution time
        getLogger().info(String.format("Attempting to invoke `%s.%s(%s)`...", className, methodName, arguments));
        stopWatch.start();
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable t) {
            result = t;
        } finally {
            stopWatch.stop();
        }
        final long elapsedTime = stopWatch.getTime(TimeUnit.MILLISECONDS);
        getLogger().info(String.format("`%s.%s(%s)` resulted in `%s` :: executed in %s ms", className, methodName, arguments, result, elapsedTime));
        return result;
    }
}