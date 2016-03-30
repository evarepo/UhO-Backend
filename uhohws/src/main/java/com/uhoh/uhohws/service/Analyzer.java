package com.uhoh.uhohws.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.uhoh.uhohws.dao.AnalysisDao;
import com.uhoh.uhohws.dao.UserDao;
import com.uhoh.uhohws.dto.UserObj;

class AnalyzerThread implements Runnable {
	UserObj user = null;
	AnalysisDao anlDao = null;
	UserDao userDao=null;

	AnalyzerThread(UserObj user, AnalysisDao anlDao, UserDao userDao) {
		this.user = user;
		this.anlDao = anlDao;
	}

	@Override
	public void run() {
		AnalyzeService svc = new AnalyzeService(anlDao, userDao);
		svc.analyzeUser(user);
	}
}

public class Analyzer {
	static ExecutorService executor = Executors.newFixedThreadPool(100);
	public static AnalysisDao anlDao = null;
	public static UserDao userDao = null;
	
	private static Runnable runnable = new Runnable() {
		public void run() {
			while (true) {
				UserObj user = (UserObj) QueueService.dequeue();

				if (user != null) {
					analyze(user, anlDao, userDao);
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	};

	public static void run() throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.execute(runnable);
	}

	private static void analyze(UserObj user, AnalysisDao anlDao, UserDao userDao) {

		Runnable task = new AnalyzerThread(user, anlDao, userDao);
		executor.execute(task);
	}
}
