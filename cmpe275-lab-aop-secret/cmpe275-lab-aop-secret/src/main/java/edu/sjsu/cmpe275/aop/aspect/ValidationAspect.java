package edu.sjsu.cmpe275.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

@Aspect
@Order(1)
public class ValidationAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */
	public static int maxLen = -1;
//	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.*(..))")
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.createSecret(..))")
	public void dummyAdvice(JoinPoint joinPoint) {
		System.out.printf("Doing validation prior to the execution of the method %s\n", joinPoint.getSignature().getName());
		
		if(joinPoint.getArgs()[0] == null)
			throw new IllegalArgumentException("Owner name cannot be empty");
		
		if (joinPoint.getArgs()[1] == null)
			throw new IllegalArgumentException("Secret message cannot be empty");
		
		String owner = joinPoint.getArgs()[0].toString();
		String secretText = joinPoint.getArgs()[1].toString();

		if (owner.length() == 0)
			throw new IllegalArgumentException("Owner name cannot be empty");
		if (secretText.length() == 0)
			throw new IllegalArgumentException("Secret message cannot be empty");
		else if(secretText.length() > 100)
			throw new IllegalArgumentException("Secret message cannot contain more than 100 characters");
		if(maxLen<secretText.length())
			maxLen = secretText.length();
	}

}
