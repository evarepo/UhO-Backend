package com.uhoh.uhohws.resource;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uhoh.uhohws.dao.AnalysisDao;
import com.uhoh.uhohws.dao.UserDao;
import com.uhoh.uhohws.dto.AnalysisResponse;
import com.uhoh.uhohws.dto.UserAnalysisResponse;
import com.uhoh.uhohws.dto.UserObj;
import com.uhoh.uhohws.dto.WebResponse;
import com.uhoh.uhohws.service.AnalyzeService;
import com.uhoh.uhohws.service.QueueService;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
	private final AtomicLong counter;
	private UserDao userDao;
	private AnalysisDao anlDao;
	private AnalyzeService anl;

	public UserResource(UserDao userDao, AnalysisDao anlDao) {
		this.userDao = userDao;
		this.counter = new AtomicLong();
		this.anl = new AnalyzeService(anlDao,userDao);
		this.anlDao = anlDao;
	}

	@POST
	public WebResponse createUser(String user) throws JsonProcessingException {

		String userId = userDao.createUser(user);
		analyzeUser(userId);
		return new WebResponse(200, "success", userId);
	}

	@GET
	public WebResponse getUser(@QueryParam("id") String id) {
		return new WebResponse(200, "success", new UserAnalysisResponse(
				userDao.getUser(id), anlDao.getUserAnalysis(id)));

	}

	@POST
	@Path("/analyze")
	@Consumes(MediaType.APPLICATION_JSON)
	public WebResponse analyzeUser(String id) throws JsonProcessingException {
		UserObj user = userDao.getUser(id);

		try {
			QueueService.enqueue(user);
			//AnalysisResponse analysis = anl.analyzeUser(user);
			return new WebResponse(200, "success", "Push notification will be sent after analysis is finished");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new WebResponse(600, "failed",
					"Server side failed due to queue overflow. Report to application admin");
		}

	}

	@POST
	@Path("/thispostisok/{userId}/{postId}/")
	@Consumes(MediaType.APPLICATION_JSON)
	public WebResponse thisPostIsOk(@PathParam("postId") String postId,
			@PathParam("userId") String userId) throws JsonProcessingException {
		anlDao.markPostAsOK(userId, postId);
		return new WebResponse(200, "success", null);
	}
}
