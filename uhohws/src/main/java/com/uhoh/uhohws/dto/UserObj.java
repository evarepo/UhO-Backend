package com.uhoh.uhohws.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

public class UserObj {

	String UOId;
	String fbToken;
	String twitterToken;
	Long lastChecked;
	String pushToken;
	String name;
	Long createdAt;
	Long lastUpdatedAt;
	Map<String, Integer> settings = new HashMap<String, Integer>();
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addSetting(String key, Integer val){
		settings.put(key, val);
	}
	
	@JsonProperty
	public Map<String, Integer> getSettings(){
		return settings;
	}

	@JsonProperty
	public String getUOId() {
		return UOId;
	}

	public void setUOId(String uOId) {
		UOId = uOId;
	}
	
	@JsonProperty
	public String getFbToken() {
		return fbToken;
	}

	public void setFbToken(String fbToken) {
		this.fbToken = fbToken;
	}

	@JsonProperty
	public String getTwitterToken() {
		return twitterToken;
	}

	public void setTwitterToken(String twitterToken) {
		this.twitterToken = twitterToken;
	}

	@JsonProperty
	public Long getLastChecked() {
		return lastChecked;
	}

	public void setLastChecked(Long lastChecked) {
		this.lastChecked = lastChecked;
	}

	@JsonProperty
	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}
	
	

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Long getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(Long lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public void setSettings(Map<String, Integer> settings) {
		this.settings = settings;
	}

	public static void main(String[] args){
		UserObj user = new UserObj();
		user.setFbToken("adsfasdfasdf");
		user.setTwitterToken("asdfadsf");
		
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(user));
		String json = "{\"fbToken\":\"adsfasdfasdf\",\"twitterToken\":\"asdfadsf\",\"lastChecked\":0}";
		UserObj x = gson.fromJson(json, UserObj.class);
		System.out.println(x.toString());
		
	}
}
