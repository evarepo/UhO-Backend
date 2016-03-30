package com.uhoh.uhohws.dao;

import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.uhoh.uhohws.dto.PostAnalysis;
import com.uhoh.uhohws.dto.UserAnalysis;
import com.uhoh.uhohws.dto.UserObj;
import com.uhoh.uhohws.service.AnalyzeService;

public class AnalysisDao extends MongoDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);

	public AnalysisDao(String host, String collection, String dbname, int port)
			throws Exception {
		super(host, collection, dbname, port);
		init();
	}

	public AnalysisDao() {

	}


	public UserAnalysis getUserAnalysis(String userId) {

		BasicDBObject o = new BasicDBObject("_id", new ObjectId(userId.trim()));
		DBObject obj = coll.findOne(o);
		if (obj == null)
			return null;
		else
			return (UserAnalysis) gson.fromJson(obj.toString(),
					UserAnalysis.class);

	}

	public void createUserAnalysis(UserAnalysis user) {

		BasicDBObject q = new BasicDBObject("_id", new ObjectId(user
				.getUserId().trim()));
		DBObject anl = coll.findOne(q);
		DBObject o = (DBObject) JSON.parse(gson.toJson(user));

		if (anl == null) {
			// create this new
			o.put("_id", new ObjectId(user.getUserId()));
			coll.insert(o);
		} else {
			// update it
			coll.update((DBObject) anl, o);
		}

	}
	
	public void markPostAsOK(String userId, String postId){
//		BasicDBObject q = new BasicDBObject("_id", new ObjectId(userId.trim()));
//		BasicDBObject anl = (BasicDBObject)coll.findOne(q);
//		
//		if(anl == null)
//			return false;
//		else{
			coll.update(new BasicDBObject("userId", userId),
	                  new BasicDBObject("$set", new BasicDBObject("analysis." + postId + ".thisIsOk", true)));
			
//		}
	}
	
	public static void main(String[] args) throws Exception{
		AnalysisDao anlDao = new AnalysisDao("localhost", "analysis", "uhoh",
				27017);
		anlDao.markPostAsOK("56e084a2b57d1830cf1e7104", "1506636821_10207466329565520");
	}
}
