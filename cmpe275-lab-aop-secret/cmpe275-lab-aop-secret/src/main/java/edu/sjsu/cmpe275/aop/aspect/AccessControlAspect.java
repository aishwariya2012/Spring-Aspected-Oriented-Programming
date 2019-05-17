package edu.sjsu.cmpe275.aop.aspect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.NotAuthorizedException;

@Aspect
@Order(1)
public class AccessControlAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */
	String ownerName = null;
	HashMap<UUID,HashSet<String>> co_owners = new HashMap<UUID,HashSet<String>>();
	HashMap<UUID,String> secretOwners = new HashMap<UUID,String>();
	public static HashMap<UUID,HashMap<String,HashSet<String>>> stats = new HashMap<UUID,HashMap<String,HashSet<String>>>();
	HashMap<UUID,HashSet<String>> reader = new HashMap<UUID,HashSet<String>>();
	
	@After("execution(public * edu.sjsu.cmpe275.aop.SecretService.createSecret(..))")
	public void setOwnerName(JoinPoint joinPoint) {
		ownerName = (String)joinPoint.getArgs()[0];
	}

	@AfterReturning(pointcut = "execution(public * edu.sjsu.cmpe275.aop.SecretService.createSecret(..))",returning="secretID")
	public void setSecretDetails(Object secretID) {
		HashSet<String> owner = new HashSet<String>();
		owner.add(ownerName);
		co_owners.put((UUID)secretID, owner);
		secretOwners.put((UUID)secretID, ownerName);
		HashMap<String,HashSet<String>> temp = new HashMap<String,HashSet<String>>();
		temp.put(ownerName, new HashSet<String>());
		stats.put((UUID)secretID,temp);
		System.out.println("Secrets HashSet is"+co_owners);
	}
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.SecretService.shareSecret(..))")
	public void validateShare(JoinPoint joinPoint) {
		String source = (String) joinPoint.getArgs()[0];
		UUID secretID=(UUID) joinPoint.getArgs()[1];
		String destSourceId = (String) joinPoint.getArgs()[2];
		if (source.length() == 0)
				throw new IllegalArgumentException("Sharer name cannot be empty");
		if (secretID == null)
			throw new IllegalArgumentException("Secret ID cannot be empty");
		if (destSourceId.length() == 0)
			throw new IllegalArgumentException("Sharee name cannot be empty");
		if(!co_owners.containsKey(secretID))
			throw new NotAuthorizedException("The secret with the given secret ID does not exist");
		else {
			if(co_owners.get(secretID).contains(source)) {
				HashSet<String> target = co_owners.get(secretID);
				target.add(destSourceId);
				co_owners.put(secretID,target);
				if(stats.get(secretID).containsKey(source)) {
					stats.get(secretID).get(source).add(destSourceId);
				}else {
					stats.get(secretID).put(source, new HashSet<String>(Arrays.asList(destSourceId)));
				}
				System.out.println("****");
				System.out.println(stats);
			}
			else {
				throw new NotAuthorizedException("User is not authorized to share the secret : "+secretID);
			}
		}
	}
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.SecretService.unshareSecret(..))")
	public void validateUnshare(JoinPoint joinPoint) {
		String source = (String) joinPoint.getArgs()[0];
		UUID secretID=(UUID) joinPoint.getArgs()[1];
		String destSourceId = (String) joinPoint.getArgs()[2];
		if (source == destSourceId)
			return;
		if (source.length() == 0)
				throw new IllegalArgumentException("Unsharer name cannot be empty");
		if (secretID == null)
			throw new IllegalArgumentException("Secret ID cannot be empty");
		if (destSourceId.length() == 0)
			throw new IllegalArgumentException("Unsharee name cannot be empty");
		if(!co_owners.containsKey(secretID))
			throw new NotAuthorizedException("The secret with the given secret ID does not exist");
		

	
		if(secretOwners.get(secretID) == source) {
			HashSet<String> target = co_owners.get(secretID);
			target.remove(destSourceId);
			co_owners.put(secretID,target);	
		}
		else {
			throw new NotAuthorizedException("User is not authorized to unshare the secret : "+secretID);
		}
		
	}
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.readSecret(..))")
	public void validateRead(JoinPoint joinPoint) {
		String source = (String) joinPoint.getArgs()[0];
		UUID secretID=(UUID) joinPoint.getArgs()[1];

		if (source.length() == 0)
				throw new IllegalArgumentException("Reader name cannot be empty");
		if (secretID == null)
			throw new IllegalArgumentException("Secret ID cannot be empty");
		
		if(!co_owners.containsKey(secretID))
			throw new NotAuthorizedException("The secret with the given secret ID does not exist");
	
		if (!co_owners.get(secretID).contains(source))
			throw new NotAuthorizedException("User is not authorized to read the secret : "+secretID);
		

		HashSet<String> read = reader.get(secretID);
		if (read == null)	{
			read = new HashSet<String>();
		}
		read.add(source);
		reader.put(secretID,read);	
		
		
		
	}

	
	
	
	
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.SecretService.*(..))")
	public void dummyAdvice(JoinPoint joinPoint) {
		System.out.printf("Access control prior to the execution of the method %s\n", joinPoint.getSignature().getName());
	}

}
