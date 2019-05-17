package edu.sjsu.cmpe275.aop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import edu.sjsu.cmpe275.aop.aspect.AccessControlAspect;
import edu.sjsu.cmpe275.aop.aspect.ValidationAspect;

public class SecretStatsImpl implements SecretStats {
    /***
     * Following is a dummy implementation.
     * You are expected to provide an actual implementation based on the requirements.
     */

	
	public void resetStatsAndSystem() {
		// TODO Auto-generated method stub
		SecretServiceImpl obj = new SecretServiceImpl();
		obj.secrets.clear();
	}

	
	public int getLengthOfLongestSecret() {
		// TODO Auto-generated method stub
		
		/*SecretServiceImpl obj = new SecretServiceImpl();
		System.out.println(obj.secrets.size());
		
		if (obj.secrets.size() == 0){
			return 0;
		}
		
		int count = 0;
		
		for(UUID key : obj.secrets.keySet()){
			String value = obj.secrets.get(key).getContent();
			if (value.length() > count)
				count = value.length();
		
		}*/
		return ValidationAspect.maxLen;
		
	}

	
	public String getMostTrustedUser() {
		TreeMap<String,Integer> map = new TreeMap<String,Integer>();
		
		for(Map.Entry<UUID,HashMap<String,HashSet<String>>> e1:AccessControlAspect.stats.entrySet()) {
			HashSet<String> hs = new HashSet<String>();
			for(Map.Entry<String, HashSet<String>> e2:e1.getValue().entrySet()) {
				hs.addAll(e2.getValue());
			}
			for(String temp:hs) {
				if(map.containsKey(temp))
					map.put(temp, map.get(temp)+1);
				else
					map.put(temp, 1);
			}
		}
		int maxVal = 0;
		String user = null;
		
		for(Map.Entry<String, Integer> e:map.entrySet()) {
			if(maxVal<e.getValue()) {
				maxVal = e.getValue();
				user = e.getKey();
			}
		}
		return user;
	}


	public String getWorstSecretKeeper() {
		TreeMap<String,Integer> map = new TreeMap<String,Integer>();
		
		for(Map.Entry<UUID,HashMap<String,HashSet<String>>> e1:AccessControlAspect.stats.entrySet()) {
			HashSet<String> hs = new HashSet<String>();
			for(Map.Entry<String, HashSet<String>> e2:e1.getValue().entrySet()) {
				hs.addAll(e2.getValue());
			}
			for(String temp:hs) {
				if(map.containsKey(temp))
					map.put(temp, map.get(temp)+1);
				else
					map.put(temp, 1);
			}
		}
		
		//HashSet<String> hs = new HashSet<String>();
		/*for(Map.Entry<UUID,HashMap<String,HashSet<String>>> e1:AccessControlAspect.stats.entrySet()) {
			hs.addAll(e1.getValue().keySet());
			for(String temp:hs) {
				if(map.containsKey(temp))
					map.put(temp, map.get(temp)-1);
				else
					map.put(temp, 0);
			}
			hs.clear();
		}*/
		for(Map.Entry<UUID,HashMap<String,HashSet<String>>> e1:AccessControlAspect.stats.entrySet()) {
			//HashSet<String> hs = new HashSet<String>();
			for(Map.Entry<String, HashSet<String>> e2:e1.getValue().entrySet()) {
				if(map.containsKey(e2.getKey()))
					map.put(e2.getKey(), map.get(e2.getKey())-e2.getValue().size());
				else
					map.put(e2.getKey(), 1-e2.getValue().size());
			}
		}
		
		int minVal = Integer.MAX_VALUE;
		String user = null;
		
		for(Map.Entry<String, Integer> e:map.entrySet()) {
			if(minVal>e.getValue()) {
				minVal = e.getValue();
				user = e.getKey();
			}
		}
		
		return user;
	}


	public String getBestKnownSecret() {
		// TODO Auto-generated method stub
		return null;
	}
    
}



