package com.api_board.restapiboard.aop;

import com.api_board.restapiboard.config.security.guard.AuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AssignMemberIdAspect {
    private final AuthHelper authHelper;

    @Before("@annotation(com.api_board.restapiboard.aop.AssignMemberId)") //부가 기능이 수행되는 지점을 지정(메소드 호출 전에 수행 되도록 설정, 본 메소드 수행전 assignMemberId 메소드 호출)
    public void assignMemberId(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
                .forEach(arg -> getMethod(arg.getClass(), "setMemberId")
                        .ifPresent(setMemberId -> invokeMethod(arg, setMemberId, authHelper.extractMemberId())));
    }

    private Optional<Method> getMethod(Class<?> clazz, String methodName) {
        try {
            return Optional.of(clazz.getMethod(methodName, Long.class));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    private void invokeMethod(Object obj, Method method, Object... args) {
        try {
            method.invoke(obj, args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
