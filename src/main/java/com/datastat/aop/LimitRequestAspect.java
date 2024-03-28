package com.datastat.aop;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.datastat.result.ResultData;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class LimitRequestAspect {

    private final ConcurrentHashMap<String, CallMark> callMarkMap = new ConcurrentHashMap<>();
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

}


