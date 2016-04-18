package com.uhoh.uhohws.jobs;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.uhoh.uhohws.dao.AnalysisDao;
import com.uhoh.uhohws.dao.UserDao;
import com.uhoh.uhohws.dto.UserObj;
import com.uhoh.uhohws.service.QueueService;

public class AnalyzeUsersJob implements Job {

	private Logger log = Logger.getLogger(AnalyzeUsersJob.class);

	public void execute(JobExecutionContext jExeCtx)
			throws JobExecutionException {
		UserDao userDao = (UserDao) jExeCtx.getJobDetail().getJobDataMap()
				.get("userDao");
		AnalysisDao anlDao = (AnalysisDao) jExeCtx.getJobDetail()
				.getJobDataMap().get("anlDao");

		System.out.println("Got DAOs ok: " + userDao.getCollection() + ";;"
				+ anlDao.getCollection());

		//check for any users that haven't been scanned in the last 6 hrs
		long since = System.currentTimeMillis() - 6*60*60*1000;
		ArrayList<UserObj> users = userDao.getOutstandingUsers(since);
		
		for(UserObj user : users){
			try {
				QueueService.enqueue(user);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}

}