package com.uhoh.uhohws.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class MongoDao {
	protected String host, collection, dbname;

	protected int port;

	protected MongoClient mongoClient;
	protected DB db;
	protected DBCollection coll;

	protected DBCursor cursor;
	protected Gson gson;
	
	public MongoDao(String host, String collection, String dbname, int port) throws Exception{
		this.host = host;
		this.collection = collection;
		this.dbname = dbname;
		this.port = port;
		init();
	}
	
	public MongoDao(){
		
	}
	
	@JsonProperty
	public String getDbname() {
		return dbname;
	}

	@JsonProperty
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	@JsonProperty
	public String getHost() {
		return host;
	}

	@JsonProperty
	public void setHost(String host) {
		this.host = host;
	}

	@JsonProperty
	public int getPort() {
		return port;
	}

	@JsonProperty
	public void setPort(int port) {
		this.port = port;
	}

	@JsonProperty
	public String getCollection() {
		return collection;
	}

	@JsonProperty
	public void setCollection(String collection) {
		this.collection = collection;
	}

	public void init() throws Exception {
		// TODO Auto-generated method stub
		mongoClient = new MongoClient(this.host, this.port);
		db = mongoClient.getDB(this.dbname);
		coll = db.getCollection(this.collection);
		gson = new Gson();
	}


}
