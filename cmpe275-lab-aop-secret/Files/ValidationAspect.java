package edu.sjsu.cmpe275.aop.aspect;

import java.util.HashSet;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.NotAuthorizedException;

@Aspect
@Order(0)
public class ValidationAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */
       public static int Maximumlength = 0;
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.createSecret(..))")
	public void checkvalidationbeforecreate(JoinPoint joinPoint) {
		System.out.printf("Doing validation prior to the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		System.out.println(joinPoint.getArgs()[0]);
		
		
		if(joinPoint.getArgs()[0] == null ){
			throw new IllegalArgumentException("Creator not authorized it cannot be null");
			
		}
		
		if(!(joinPoint.getArgs()[0] == null) )
		  {
			if(joinPoint.getArgs()[0].toString().length() == 0){
				
				throw new IllegalArgumentException("Creator not authorized its, length is Zero");
			}
		  }
	
		
		
		
		if(! (joinPoint.getArgs()[1] == null)){
			
			String SecretContent = joinPoint.getArgs()[1].toString();
     		int length = SecretContent.length();
			
			if(length>100)
				throw new IllegalArgumentException("Secret Cannot be more than 100 characters , create again with valid values");
			
			if(length>Maximumlength)
				Maximumlength = length;
			
		 }
		
		
	}
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.readSecret(..))")
	 public void readasecret(JoinPoint joinPoint){
		
		if(joinPoint.getArgs()[0] == null)
			throw new IllegalArgumentException("Reader name cannot be null");
			
		if(joinPoint.getArgs()[1] == null){
			throw new IllegalArgumentException("secretID cannot be null");
		 }
		
		else{
			
			String sourceowner = joinPoint.getArgs()[0].toString();
			UUID secretID = (UUID) joinPoint.getArgs()[1];
			
			
			if(sourceowner.length()== 0 ){
				throw new IllegalArgumentException("Reader name cannot be of length Zero, enter valid names");
			
		}
	}
	}

	
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.shareSecret(..))")
	 public void shareasecret(JoinPoint joinPoint){
		
		if(joinPoint.getArgs()[0] == null)
			throw new IllegalArgumentException("Secret Sharer name cannot be null");
			
		if(joinPoint.getArgs()[1] == null){
			throw new IllegalArgumentException("secretID cannot be null");
		 }
		
		if(joinPoint.getArgs()[2] == null)
			throw new IllegalArgumentException("Secret Receiver name cannot be null");
	    
		else{
		String sourceowner = joinPoint.getArgs()[0].toString();
		UUID secretID = (UUID) joinPoint.getArgs()[1];
		String  secretsharedwith =  joinPoint.getArgs()[2].toString();
		
		if (sourceowner.length() == 0 )
			throw new IllegalArgumentException("Secret Sharer of secret share cant be empty(i.e Argument length=0)");
		
	 	 if (secretsharedwith.length() == 0 )
		   	throw new IllegalArgumentException("Secret Receiver name cannot be empty(i.e Argument length=0)");
		}
	}
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.unshareSecret(..))")
	 public void unshareasecret(JoinPoint joinPoint){
		
		if(joinPoint.getArgs()[0] == null)
			throw new IllegalArgumentException("Secret Sharer name cannot be null");
			
		if(joinPoint.getArgs()[1] == null){
			throw new IllegalArgumentException("secretID cannot be null");
		 }
		
		if(joinPoint.getArgs()[2] == null)
			throw new IllegalArgumentException("Secret Receiver name cannot be null");
	    
		else{
		String sourceowner = joinPoint.getArgs()[0].toString();
		UUID secretID = (UUID) joinPoint.getArgs()[1];
		String  secretunsharedwith =  joinPoint.getArgs()[2].toString();
		
		if (sourceowner.length() == 0 )
			throw new IllegalArgumentException("The Source who is unsharing secret, name cant be empty(i.e Argument length=0)");
		
	 	 if (secretunsharedwith.length() == 0 )
		   	throw new IllegalArgumentException("Secret to UnShare with, name cannot be empty(i.e Argument length=0)");
		}
	}
}
