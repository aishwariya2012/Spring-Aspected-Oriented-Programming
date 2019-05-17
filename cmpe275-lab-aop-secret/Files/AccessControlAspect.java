package edu.sjsu.cmpe275.aop.aspect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
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
	String secretcreator = null;
	String content=null;
	
	public static HashMap<UUID,HashMap<String,HashSet<String>>> StatisticalMap = new HashMap<UUID,HashMap<String,HashSet<String>>>();
	
	public static TreeMap<String,HashSet<String>> secretsread=new  TreeMap<String,HashSet<String>>();
	HashMap<UUID,HashSet<String>> secretotherowner = new HashMap<UUID,HashSet<String>>();
	HashMap<UUID,String> secretOwner = new HashMap<UUID,String>();
	
	public static HashMap<UUID,String> secrets = new HashMap<UUID,String>();

	@Before("execution(public void edu.sjsu.cmpe275.aop.SecretService.*(..))")
	public void dummyAdvice(JoinPoint joinPoint) {
		System.out.printf("Access control prior to the executuion of the metohd %s\n", joinPoint.getSignature().getName());
	}

	@After("execution(public * edu.sjsu.cmpe275.aop.SecretService.createSecret(..))")
	public void Ownername(JoinPoint joinPoint) {
		 secretcreator = joinPoint.getArgs()[0].toString();
		 if(!(joinPoint.getArgs()[1] == null))
		    content=joinPoint.getArgs()[1].toString();
		 else
			 content=null;
		 
		
		 
	}
	

	@AfterReturning(pointcut = "execution(public * edu.sjsu.cmpe275.aop.SecretService.createSecret(..))",returning="secretID")
	public void setSecretOwnerDetails(Object secretID) {
		HashSet<String> secretowners = new HashSet<String>();
		secretowners.add(secretcreator);
		secretotherowner.put((UUID)secretID, secretowners);
		secretOwner.put((UUID)secretID, secretcreator);
		
		HashSet<String> secretshared = new HashSet<String>();
		HashMap<String,HashSet<String>> map = new HashMap<String,HashSet<String>>();
		
		map.put(secretcreator, secretshared);
		StatisticalMap.put((UUID) secretID,map);
		
		
		secrets.put((UUID)secretID,content);
		
	}
	
	
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.readSecret(..))")
	 public void readasecret(JoinPoint joinPoint){
		String sourceowner = joinPoint.getArgs()[0].toString();
		UUID secretID = (UUID) joinPoint.getArgs()[1];
	
		if(!secretOwner.containsKey(secretID)){
			throw new NotAuthorizedException("The secret with the given secret ID does not exist");

		}
		if(!secretotherowner.get(secretID).contains(sourceowner))
		{
			throw new NotAuthorizedException("The user is not authorized to read secret, neither has he created or shared the secret="+secretID);
		}
		
	
		String Content = secrets.get(secretID);
	
		
		if(! (Content==null)){
			
			HashSet<String> read = secretsread.get(Content);
			
			if(!(secretOwner.get(secretID)==sourceowner)){
			     if (read == null)	{
				     read = new HashSet<String>();
				     read.add(sourceowner);
			     }
			     else{
			    	 read.add(sourceowner);
			 	 
			     }
			     
			 	secretsread.put(Content,read);
			 	
			}
			
		}
		
	
		
	}
	
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.shareSecret(..))")
	 public void shareasecret(JoinPoint joinPoint){
		String sourceowner = joinPoint.getArgs()[0].toString();
		UUID secretID = (UUID) joinPoint.getArgs()[1];
		String  secretsharedwith =  joinPoint.getArgs()[2].toString();
		
		if(!secretOwner.containsKey(secretID))
			throw new NotAuthorizedException("The secret with the given secret ID is not found");
		
		if(secretotherowner.get(secretID).contains(sourceowner) && sourceowner == secretsharedwith )
				return ;

		if(!secretotherowner.get(secretID).contains(sourceowner))
		    throw new NotAuthorizedException("The person has been not authorized to share secret="+secretID);
		
		
		else if(secretotherowner.get(secretID).contains(sourceowner)){
			System.out.println("Entered here");
				HashSet<String> temp = secretotherowner.get(secretID);
				  temp.add(secretsharedwith);
				secretotherowner.put(secretID, temp);
				
			
				
				if(StatisticalMap.get(secretID).containsKey(sourceowner)) 
					StatisticalMap.get(secretID).get(sourceowner).add(secretsharedwith);
				
				else {
					HashSet<String> temp1 = new HashSet<String> ();
					temp1.add(secretsharedwith);
					StatisticalMap.get(secretID).put(sourceowner,temp1); 
				    }
		 }
		
	
		
		
	} 
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.unshareSecret(..))")
	 public void unshareasecret(JoinPoint joinPoint){
		String sourceowner = joinPoint.getArgs()[0].toString();
		UUID secretID = (UUID) joinPoint.getArgs()[1];
		String  secretunsharedwith =  joinPoint.getArgs()[2].toString();
		
     	if(!secretOwner.containsKey(secretID))
			throw new NotAuthorizedException("The secret with the given secret ID is not found");
		
		if(secretotherowner.get(secretID).contains(sourceowner) && sourceowner == secretunsharedwith )
			return ;
		
		if(secretOwner.get(secretID) == sourceowner) {
			HashSet<String> temp = secretotherowner.get(secretID);
			temp.remove(secretunsharedwith);
			secretotherowner.put(secretID,temp);	
			
			
			
		}
		else {
			throw new NotAuthorizedException("User is not authorized to unshare this particular secret : "+secretID);
		}
		
	
		
	}
	
}
