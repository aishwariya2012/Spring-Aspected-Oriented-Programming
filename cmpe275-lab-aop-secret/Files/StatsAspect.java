package edu.sjsu.cmpe275.aop.aspect;

import org.aspectj.lang.JoinPoint;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.SecretStatsImpl;
import edu.sjsu.cmpe275.aop.aspect.ValidationAspect;

@Aspect
@Order(0)
public class StatsAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

	//@Autowired SecretStatsImpl stats;
	public static int Maximumlength = 0;
	
	@After("execution(public void edu.sjsu.cmpe275.aop.SecretService.*(..))")
	public void dummyAfterAdvice(JoinPoint joinPoint) {
		System.out.printf("After the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		
	}
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.SecretService.*(..))")
	public void dummyBeforeAdvice(JoinPoint joinPoint) {
		System.out.printf("Doing stats before the executuion of the metohd %s\n", joinPoint.getSignature().getName());
	}
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretStats.getLengthOfLongestSecret(..))")
	public void calllength(JoinPoint joinPoint) {
		
		Maximumlength = ValidationAspect.Maximumlength;
	}
	
	
	
	
}
