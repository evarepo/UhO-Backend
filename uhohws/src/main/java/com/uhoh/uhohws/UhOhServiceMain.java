package com.uhoh.uhohws;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.uhoh.uhohws.conf.UhOhConfiguration;
import com.uhoh.uhohws.dao.AnalysisDao;
import com.uhoh.uhohws.dao.UserDao;
import com.uhoh.uhohws.jobs.AnalyzeUsersJob;
import com.uhoh.uhohws.resource.UserResource;
import com.uhoh.uhohws.service.Analyzer;

// public class EvaServiceMain extends Service<ServiceConfiguration> {
public class UhOhServiceMain extends Application<UhOhConfiguration> {

	public static void main(String[] args) throws Exception {
		new UhOhServiceMain().run(args);
	}

	@Override
	public String getName() {
		return "hello-world";
	}

	@Override
	public void initialize(Bootstrap<UhOhConfiguration> bootstrap) {
		// bootstrap.setName("UhOh-REST-Services");
	}

	private void runScheduler(UserDao userDao, AnalysisDao anlDao) throws Exception {
		try {

			// specify the job' s details..
			JobDataMap map = new JobDataMap();
			map.put("userDao", userDao);
			map.put("anlDao", anlDao);
			JobDetail job = JobBuilder.newJob(AnalyzeUsersJob.class)
					.usingJobData(map)
					.withIdentity("AnalyzeUsersJob").build();

			// specify the running period of the job
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withSchedule(
							SimpleScheduleBuilder.simpleSchedule()
									.withIntervalInHours(1).repeatForever())
					.build();

			// schedule the job
			SchedulerFactory schFactory = new StdSchedulerFactory();
			Scheduler sch = schFactory.getScheduler();
			sch.start();
			sch.scheduleJob(job, trigger);

		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

	public void run(final UhOhConfiguration conf, final Environment env)
			throws Exception {

		UserDao userDao = conf.getUserDao();
		userDao.init();
		AnalysisDao anlDao = conf.getAnlDao();
		anlDao.init();
		Analyzer.anlDao = anlDao;
		Analyzer.userDao = userDao;
		Analyzer.run();
		final UserResource userRes = new UserResource(userDao, anlDao);
		env.jersey().register(userRes);
		runScheduler(userDao, anlDao);
	}

}
