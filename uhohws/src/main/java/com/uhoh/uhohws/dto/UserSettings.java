package com.uhoh.uhohws.dto;

import java.util.HashMap;
import java.util.Map;

public class UserSettings {

	Map<String, Integer> settings = new HashMap<String, Integer>();
	
	public void addSetting(String key, Integer val){
		settings.put(key, val);
	}
	
	public Map<String, Integer> getSettings(){
		return settings;
	}
}
