package edu.sjsu.cmpe275.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Around;
import java.io.IOException;

@Aspect
@Order(1)
public class RetryAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */
 
	@Around("execution(public void edu.sjsu.cmpe275.aop.SecretService.*(..))")
	public void dummyAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.printf("Retry aspect prior to the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		Object result = null;
		try {
			result = joinPoint.proceed();
			System.out.printf("Finished the execution of the method %s\n", joinPoint.getSignature().getName());
		} catch (IOException temp) {
			temp.printStackTrace();
			System.out.printf("Aborted the executuion of the method %s\n", joinPoint.getSignature().getName());
			System.out.printf("Retrying for the first time.");
			try {
				result = joinPoint.proceed();
				System.out.printf("Finished the execution of the method %s\n", joinPoint.getSignature().getName());
			}
			catch(IOException temp2) {
				System.out.printf("Retrying for the second time.");
				try {
					result = joinPoint.proceed();
					System.out.printf("Finished the execution of the method %s\n", joinPoint.getSignature().getName());
				}
				catch(IOException temp3) {
					System.out.printf("Network error. Cannot Move Further with the Operation");
				}
			}
		}
	}


}
