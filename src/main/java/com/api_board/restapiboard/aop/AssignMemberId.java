package com.api_board.restapiboard.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * aop를 이용하여 기존 코드를 건드리지 않고 새로운 부가 기능을 추가 할 수 있다
 * 컨트롤러의 코드 수정 없이 요청이 바인딩된 컨트롤러 파라미터에 memberId를 주입 할 수 있게 해준다
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface AssignMemberId {
}
