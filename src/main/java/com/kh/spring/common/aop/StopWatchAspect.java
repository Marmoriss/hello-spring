package com.kh.spring.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class StopWatchAspect {

	@Pointcut("execution(* com.kh.spring.todo.controller.TodoController.insertTodo(..))")
	public void pointcut() {}
	
	@Around("pointcut()")
	public Object stopWatchAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		// StopWatch 생성
		StopWatch stopwatch = new StopWatch();
		
		// 타이머 시작 (Before)
		stopwatch.start();
		
		// 작업
		Object obj = joinPoint.proceed();
		
		// 타이머 종료 (After)
		stopwatch.stop();
		
		// 결과 분석
		long milis = stopwatch.getTotalTimeMillis();
		log.debug("insertTodo 소요시간 : {}ms", milis);
		
		return obj;
	}
	
}
