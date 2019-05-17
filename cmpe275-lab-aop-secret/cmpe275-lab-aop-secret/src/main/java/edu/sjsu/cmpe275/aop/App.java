package edu.sjsu.cmpe275.aop;

import java.util.UUID;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        /***
         * Following is a dummy implementation of App to demonstrate bean creation with Application context.
         * You may make changes to suit your need, but this file is NOT part of your submission.
         */

    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
        SecretService secretService = (SecretService) ctx.getBean("secretService");
        SecretStats stats = (SecretStats) ctx.getBean("secretStats");

        try {
        	UUID secret1 = secretService.createSecret("Alice", "My little secret");
        	UUID secret2 = secretService.createSecret("Bob", "My new secret");
        	UUID secret3 = secretService.createSecret("Bob", "My new secret");
        	secretService.shareSecret("Alice",secret1, "Bob");
        	secretService.readSecret("Alice", secret1);
        	secretService.readSecret("Bob", secret1);
//        	secretService.shareSecret("Alice",secret1, "Bob");
//        	secretService.shareSecret("Bob",secret1, "Carl");
//        	secretService.unshareSecret("Alice",secret1, "Carl");
//        	secretService.readSecret("Carl", secret1);
//        	secretService.shareSecret("Alice",secret1, "Bob");
//        	secretService.shareSecret("Bob", secret2, "Carl");
//        	secretService.shareSecret("Bob", secret2, "Alice");
        	
        	
        	

//        	secretService.readSecret("Alice", secret1);
//        	secretService.shareSecret("Carl", secret1, "Dan");
//        	secretService.shareSecret("Dan", secret1, "Earnest");
//        	secretService.shareSecret("Rohan", secret, "Bob");
//        	secretService.readSecret("Bob", secret1);
        } catch (Exception e) {
            e.printStackTrace();
        }
System.out.println(stats.getLengthOfLongestSecret());
      System.out.println("Best known secret: " + stats.getBestKnownSecret());
        System.out.println("Worst secret keeper: " + stats.getWorstSecretKeeper());
        System.out.println("Most trusted user: " + stats.getMostTrustedUser());
        ctx.close();
    }
}
