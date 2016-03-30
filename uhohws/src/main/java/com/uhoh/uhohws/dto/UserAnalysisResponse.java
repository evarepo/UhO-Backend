package com.uhoh.uhohws.dto;

public class UserAnalysisResponse {

	UserObj userDetails;
	UserAnalysis userAnalysis;
	
	public UserAnalysisResponse(UserObj user, UserAnalysis analysis) {
		super();
		this.userDetails = user;
		this.userAnalysis = analysis;
	}

	public UserObj getUser() {
		return userDetails;
	}

	public void setUser(UserObj user) {
		this.userDetails = user;
	}

	public UserAnalysis getAnalysis() {
		return userAnalysis;
	}

	public void setAnalysis(UserAnalysis analysis) {
		this.userAnalysis = analysis;
	}
	
}
