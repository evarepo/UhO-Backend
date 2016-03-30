package com.uhoh.uhohws.conf;

import io.dropwizard.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uhoh.uhohws.dao.AnalysisDao;
import com.uhoh.uhohws.dao.UserDao;

public class UhOhConfiguration extends Configuration {

	private UserDao userDao;
	private AnalysisDao anlDao;


    @JsonProperty("usersdb")
	public void setUserDB(UserDao userDao) {
		this.userDao = userDao;
	}

    @JsonProperty("analysisdb")
	public void setAnalysisDB(AnalysisDao anlDao) {
		this.anlDao = anlDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public AnalysisDao getAnlDao() {
		return anlDao;
	}
    
}