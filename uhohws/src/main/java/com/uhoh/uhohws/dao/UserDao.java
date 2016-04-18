package com.uhoh.uhohws.dao;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.uhoh.uhohws.dto.UserObj;
import com.uhoh.uhohws.service.FacebookService;


public class UserDao extends MongoDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);

	public UserDao(String host, String collection, String dbname, int port)
			throws Exception {
		super(host, collection, dbname, port);
		init();
	}
	
	public UserDao() {
        
    }
	
	public String createUser(String user) throws JsonProcessingException{
//		ObjectMapper mapper = new ObjectMapper();
//		String json = mapper.writeValueAsString(user);
		System.out.println("input string: \n" + user);
		UserObj obj = gson.fromJson(user, UserObj.class);
		obj.setLastChecked(0L);
		BasicDBObject userObj = (BasicDBObject) JSON.parse(user);
		Long now = System.currentTimeMillis();
		userObj.put("lastUpdatedAt", now);
		String id = ""; 
		
		FacebookService fbSvc = new FacebookService(obj.getFbToken());
		String name = fbSvc.getName();
		
		
		//see if user exists, then update
		if (obj.getUOId() != null && obj.getUOId().length() > 0){
			//existing user, update the user
			BasicDBObject dbo = new BasicDBObject();
			dbo.append("_id", new ObjectId(obj.getUOId()));
			coll.update(dbo, (DBObject) userObj)  ;
			id = obj.getUOId();
			
		}else{
			// create user
			ObjectId iid = new ObjectId();
			userObj.append("UOId", iid.toString());
			userObj.append("_id", iid);
			userObj.append("lastChecked", 0L);
			userObj.put("createdAt", now);
			userObj.put("name", name);
			BasicDBObject settings = new BasicDBObject();
			settings.append("anger", 0);
			settings.append("foul language", 0);
			settings.append("political correctness", 0);
			settings.append("partying", 0);
			settings.append("monitoring", 1);
			userObj.append("settings", settings);
			coll.insert(userObj);
			ObjectId oid = (ObjectId)userObj.get( "_id" );
			id = oid.toString();
		}
		
		
		return id;
	}
	
	
	public UserObj getUser(String UOId){
		BasicDBObject o = new BasicDBObject("_id", new ObjectId(UOId.trim()));
		DBObject obj = coll.findOne(o);	
		return (UserObj)gson.fromJson(obj.toString(), UserObj.class);
	}
	
	public ArrayList<UserObj> getOutstandingUsers(long since){
		//{"lastChecked" : { $lte : 99}}
		BasicDBObject qry = new BasicDBObject("lastChecked", new BasicDBObject("$lte", since)  );
		DBCursor cur = coll.find(qry);
		ArrayList<UserObj> arr = new ArrayList<UserObj>();  
		while(cur.hasNext()){
			DBObject user = cur.next();
			UserObj obj = (UserObj)gson.fromJson(user.toString(), UserObj.class);
			arr.add(obj);
		}

		return arr;
	}

	public void updateLastCheckedTime(String UOId, Long time){
		BasicDBObject o = new BasicDBObject("_id", new ObjectId(UOId.trim()));
		DBObject obj = coll.findOne(o);	
		Long now = time > 0L ? time : System.currentTimeMillis(); 
		obj.put("lastChecked", now);
		coll.update(o, obj);
	}
	
	public void analyzeTwitter(Long lastChecked, String twitterToken){
		
	}
}
