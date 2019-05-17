package edu.sjsu.cmpe275.aop;

import edu.sjsu.cmpe275.aop.aspect.StatsAspect;
import edu.sjsu.cmpe275.aop.aspect.AccessControlAspect;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class SecretStatsImpl implements SecretStats {
    /***
     * Following is a dummy implementation.
     * You are expected to provide an actual implementation based on the requirements.
     */

	
	public void resetStatsAndSystem() {
		SecretServiceImpl resetsecrets = new SecretServiceImpl();
		resetsecrets.secrets.clear();
		AccessControlAspect.StatisticalMap.clear();
		
		
	}


	public int getLengthOfLongestSecret() {
		
		
	
		return StatsAspect.Maximumlength;
	}


	public String getMostTrustedUser() {
		TreeMap<String,Integer> TrustedUsers = new TreeMap<String,Integer>();
		
		for(Map.Entry<UUID,HashMap<String,HashSet<String>>> temp1:AccessControlAspect.StatisticalMap.entrySet()){
			HashSet<String> temp2 = new HashSet<String>();
			
			for(Map.Entry<String, HashSet<String>> temp3: temp1.getValue().entrySet()) {
				
				
				for(String s:temp3.getValue()){
					if(! (s == temp3.getKey())){
						if(TrustedUsers.containsKey(s))
							TrustedUsers.put(s, TrustedUsers.get(s)+1);
						else
							TrustedUsers.put(s, 1);
					}
				}
			 
			
				
			}
			
			
		}
		
	
		int mosttrustedusercount=0;
		String mosttrusteduser = null;
		
		for(Map.Entry<String, Integer> temp4:TrustedUsers.entrySet()) {
			if(mosttrustedusercount<temp4.getValue()) {
				mosttrustedusercount = temp4.getValue();
				mosttrusteduser = temp4.getKey();
			}
		}
		return mosttrusteduser;
	}

	public String getWorstSecretKeeper() {
		TreeMap<String,Integer> WorstSecretKeeper =new TreeMap<String,Integer> ();	
		
		for(Map.Entry<UUID,HashMap<String,HashSet<String>>> temp1:AccessControlAspect.StatisticalMap.entrySet()){
			
			
			for(Map.Entry<String, HashSet<String>> temp3: temp1.getValue().entrySet()) {
			
				
				for(String s:temp3.getValue()){
					if(! (s == temp3.getKey())){
						if(WorstSecretKeeper.containsKey(s))
							WorstSecretKeeper.put(s, WorstSecretKeeper.get(s)+1);
						else
							WorstSecretKeeper.put(s, 1);
					}
				}
		     }
         }
		
		
		for(Map.Entry<UUID,HashMap<String,HashSet<String>>> temp1:AccessControlAspect.StatisticalMap.entrySet()){
			
			for(Map.Entry<String, HashSet<String>> temp3: temp1.getValue().entrySet()) {
			
				if(WorstSecretKeeper.containsKey(temp3.getKey()))
				   WorstSecretKeeper.put(temp3.getKey(),WorstSecretKeeper.get(temp3.getKey()) - temp3.getValue().size());
				else
					WorstSecretKeeper.put(temp3.getKey(), 0 - temp3.getValue().size());
			  }
			
		}
	
		int worstsecretkeepercount=Integer.MAX_VALUE;
		String worstsecretkeeper = null;
		
		for(Map.Entry<String, Integer> temp4:WorstSecretKeeper.entrySet()) {
			if(worstsecretkeepercount>temp4.getValue()) {
				worstsecretkeepercount = temp4.getValue();
				worstsecretkeeper = temp4.getKey();
			}
		}
		return worstsecretkeeper;

		
	
	}
	


	public String getBestKnownSecret() {
		int count=0;
		String mostreadcontent = null;
		
		System.out.println(AccessControlAspect.secretsread);
		for(Map.Entry<String,HashSet<String>> temp1:AccessControlAspect.secretsread.entrySet())
		{
			System.out.println(temp1);
			if(temp1.getValue().size()>count){
				count=temp1.getValue().size();
				mostreadcontent=temp1.getKey();
				
			}
		}
		
//		for(Map.Entry<UUID, String> temp2:AccessControlAspect.secrets.entrySet()){
//			if(temp2.getKey() == secretID){
//				mostreadcontent=temp2.getValue();
//			}
//			
//		}
		
	
		return mostreadcontent;
	}
    
}



