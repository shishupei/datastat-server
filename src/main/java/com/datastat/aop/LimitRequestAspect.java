package com.datastat.aop;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.datastat.result.ResultData;
import com.datastat.util.ClientUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LimitRequestAspect {

    @Value("${request_interval:60000}")
    String request_interval;

    private final ConcurrentHashMap<String, CallMark> callMarkMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> ipAccessCount = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("@annotation(limitRequest)")
    public void exudeService(LimitRequest limitRequest) {}


    @Around(value = "exudeService(limitRequest)", argNames = "joinPoint,limitRequest")
    public Object before(ProceedingJoinPoint joinPoint, LimitRequest limitRequest) throws Throwable {
        if (!isAllowed(joinPoint.getSignature().getName(), limitRequest)) {
            ResultData resultData = ResultData.fail(HttpStatus.TOO_MANY_REQUESTS.value(), "Too Many Requests");
            return objectMapper.writeValueAsString(resultData);
        }

        return joinPoint.proceed();
    }


    public boolean isAllowed(String methodName, LimitRequest limitRequest) {
        Duration timeWindow = Duration.ofSeconds(limitRequest.callTime());
        Instant now = Instant.now();
        if (callMarkMap.containsKey(methodName)) {
            CallMark callMark = callMarkMap.get(methodName);

            if (Duration.between(callMark.getLastCallTime(), now).compareTo(timeWindow) > 0) {
                callMark.setLastCallTime(now);
                callMark.setCallCount(0);
            }
            
            if (callMark.getCallCount() < limitRequest.callCount()) {
                callMark.setCallCount(callMark.getCallCount() + 1);
                callMarkMap.put(methodName, callMark);
                return true;
            }
            return false;

        } else {
            CallMark callMark = new CallMark();
            callMark.setLastCallTime(now);
            callMark.setCallCount(1);
            callMarkMap.put(methodName, callMark);
            return true;
        } 
    }

    @Around("@annotation(RateLimit)")
    public Object limitIpAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (!(attributes instanceof ServletRequestAttributes)) {
            throw new RuntimeException("Not a web application");
        }

        HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
        String ip = ClientUtil.getClientIpAddress(request);
        if (ipAccessCount.containsKey(ip)) {
            long lastAccessTime = ipAccessCount.get(ip);
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastAccessTime < Long.parseLong(request_interval)) {
                ResultData resultData = ResultData.fail(HttpStatus.TOO_MANY_REQUESTS.value(), "Submit too frequently, please try again later");
                return objectMapper.writeValueAsString(resultData);
            }
        }
        ipAccessCount.put(ip, System.currentTimeMillis());
        return joinPoint.proceed();
    }

}


