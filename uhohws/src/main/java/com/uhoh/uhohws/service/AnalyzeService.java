package com.uhoh.uhohws.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.uhoh.uhohws.conf.UOConstants;
import com.uhoh.uhohws.dao.AnalysisDao;
import com.uhoh.uhohws.dao.UserDao;
import com.uhoh.uhohws.dto.AnalysisResponse;
import com.uhoh.uhohws.dto.ImageNode;
import com.uhoh.uhohws.dto.ImagesAnalysis;
import com.uhoh.uhohws.dto.PostAnalysis;
import com.uhoh.uhohws.dto.UOObject;
import com.uhoh.uhohws.dto.UserAnalysis;
import com.uhoh.uhohws.dto.UserObj;
import com.uhoh.uhohws.service.ToneAnalyzerService.Analysis;

public class AnalyzeService {

	ToneAnalyzerService toneSvc;
	PictureAnalyzerService picSvc;
	AnalysisDao anlDao;
	UserDao userDao;

	public static class ObjectAnalysis {
		UOObject obj;
		Analysis tones;

		public ObjectAnalysis(UOObject obj, Analysis tones) {
			super();
			this.obj = obj;
			this.tones = tones;
		}

		public UOObject getObj() {
			return obj;
		}

		public void setObj(UOObject obj) {
			this.obj = obj;
		}

		public Analysis getTones() {
			return tones;
		}

		public void setTones(Analysis tones) {
			this.tones = tones;
		}

	}

	public AnalyzeService(AnalysisDao anlDao, UserDao userDao) {
		toneSvc = new ToneAnalyzerService();
		picSvc = new PictureAnalyzerService();
		this.anlDao = anlDao;
		this.userDao = userDao;
	}

	public static class PicSvcThread implements Callable<ImagesAnalysis> {
		String imgName;
		PictureAnalyzerService picSvc;

		public PicSvcThread(String input, PictureAnalyzerService picSvc) {
			this.imgName = input;
			this.picSvc = picSvc;
		}

		@Override
		public ImagesAnalysis call() throws Exception {
			return picSvc.analyzeImageBundle(imgName);
		}
	}

	private void addToAnalysisMap(Map<String, PostAnalysis> analysis,
			UOObject obj, String type) {
		PostAnalysis anl = null;
		if (analysis.containsKey(obj.getPostId())) {
			anl = analysis.get(obj.getPostId());
		} else {
			anl = new PostAnalysis();
		}
		if (type.equals("PIC")) {
			anl.setPostId(obj.getPostId());
			anl.setPicURL(obj.getPostPic());
			anl.setPicComment(UOConstants.BAD_PIC);
			analysis.put(obj.getPostId(), anl);
		} else if (type.equals("POST")) {

			anl.setPostId(obj.getPostId());
			anl.setPostMsg(obj.getPostMsg());
			anl.setPostMsgComment(UOConstants.BAD_POST_MSG);
			analysis.put(obj.getPostId(), anl);

		} else if (type.equals("COMMENT")) {
			anl.addComment(obj.getCommentId(), UOConstants.BAD_POST_MSG);
			analysis.put(obj.getPostId(), anl);
		}

	}

	public AnalysisResponse analyzeUser(UserObj user) {

		String fbToken = user.getFbToken();
		Long lastChecked = user.getLastChecked();
		Map<String, ArrayList<ObjectAnalysis>> negMap = new HashMap<String, ArrayList<ObjectAnalysis>>();
		Set<String> all = new HashSet<String>();
		Map<String, UOObject> picsMap = new HashMap<String, UOObject>();
		Map<String, PostAnalysis> anlMap = new HashMap<String, PostAnalysis>();

		FacebookService fbSvc = new FacebookService(fbToken);
		String myId = fbSvc.getMyId(fbToken);
		String basePath = "/tmp/" + myId;
		File f = new File(basePath);
		f.mkdir();

		UserAnalysis currAnls = anlDao.getUserAnalysis(user.getUOId());
		UserAnalysis anls = new UserAnalysis();
		anls.setUserId(user.getUOId());
		anls.setAnalysis(anlMap);
		
		ArrayList<UOObject> data = fbSvc.getContentsOfUser(lastChecked);

		ExecutorService executorService = Executors.newFixedThreadPool(10);

		int i =0;
		for (UOObject obj : data) {

			ObjectAnalysis anl = null;
			if (obj.getType() == UOConstants.POST_TYPE) {

				// save pic file and send it to visual analysis service
				if (obj.getPostPic() != null) {
					if (obj.getPostPic().length() > 0) {
						try {
							String name = saveImage(obj.getPostPic(), f);
							Future<ImagesAnalysis> result = executorService
									.submit(new PicSvcThread(name, picSvc));
							// ImagesAnalysis iAnl =
							// picSvc.analyzeImageBundle(name);
							ImagesAnalysis iAnl = result.get(120,
									TimeUnit.SECONDS);
							if (iAnl != null) {
								ImageNode n = iAnl.getImages().get(0);
								if (n != null && n.getScores() != null) {
									if (n.getScores().size() > 0) {
										if (n.getScores().get(0).getScore() > 0.61) {
											addToAnalysisMap(anlMap, obj, "PIC");
											anls.setNegPhotosSplit(anls.getNegPhotosSplit() + 1);
											System.out.println("name: "
													+ name
													+ "; score: "
													+ n.getScores().get(0)
															.getScore());
										}
									}
								}
							}
							picsMap.put(name, obj);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				// analyze text
				String msg = obj.getPostMsg();
				if (msg == null || msg.length() == 0)
					continue;

				Analysis tones = toneSvc.isStrong(msg);
				executorService.execute(new ToneAnalyzerService.MyThread(
						toneSvc, msg));
				if (tones.tones.size() == 0)
					continue;
				else {
					anl = new ObjectAnalysis(obj, tones);
					if (anl.getTones().sentiment.equals("NEG")) {
						addToAnalysisMap(anlMap, obj, "POST");
						anls.setNegCommentsSplit(anls.getNegCommentsSplit() + 1);
					}
				}

			} else if (obj.getType() == UOConstants.COMMENT_TYPE) {
				// analyze text
				String msg = obj.getCommentMsg();
				if (msg == null || msg.length() == 0)
					continue;
				Analysis tones = toneSvc.isStrong(msg);
				if (tones.tones.size() == 0)
					continue;
				else {
					anl = new ObjectAnalysis(obj, tones);
					if (anl.getTones().sentiment.equals("NEG")) {
						addToAnalysisMap(anlMap, obj, "COMMENT");
						anls.setNegCommentsSplit(anls.getNegCommentsSplit() + 1);

					}
				}
			} else {

			}

			if (anl.getTones().sentiment.equals("NEG")) {
				if (negMap.containsKey(obj.getPostId())) {
					ArrayList<ObjectAnalysis> list = negMap
							.get(obj.getPostId());
					list.add(anl);
				} else {
					ArrayList<ObjectAnalysis> list = new ArrayList<ObjectAnalysis>();
					list.add(anl);
					negMap.put(obj.getPostId(), list);
				}
			}

			// get all unique posts
			all.add(obj.getPostId());
			i++;
			
			if(i>10){
				break;
			}
		}

		int totalPosts = all.size();
		int badPosts = anls.getAnalysis().size();
		int goodPosts = totalPosts - badPosts;
		
		anls.setPosSplit(goodPosts);
		anls.setNegSplit(badPosts);
		anls.setTotalPosts(totalPosts);

		if(currAnls != null){
			mergeAnalysis(currAnls, anls);
			
			
			anlDao.createUserAnalysis(currAnls);
		}
		else{
			anlDao.createUserAnalysis(anls);
		}
		
		userDao.updateLastCheckedTime(user.getUOId(), System.currentTimeMillis());
		return new AnalysisResponse(negMap, all.size());
	}
	
	private void mergeAnalysis(UserAnalysis old, UserAnalysis now){
		if(old != null){
			now.getAnalysis().forEach(old.getAnalysis()::putIfAbsent);
			
			old.setTotalPosts(old.getTotalPosts() + now.getTotalPosts());
			old.setPosSplit(old.getPosSplit() + now.getPosSplit());
			old.setNegSplit(old.getNegSplit() + now.getNegSplit());
			old.setNegCommentsSplit(old.getNegCommentsSplit() + now.getNegCommentsSplit());
			old.setNegPhotosSplit(old.getNegPhotosSplit() + now.getNegPhotosSplit());
			old.setNegVideoSplit(old.getNegVideoSplit() + now.getNegVideoSplit());
		}
		
	}
	private String saveImage(String imageUrl, File dir) throws IOException {
		String destinationFile = getPictureName(imageUrl);
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(dir + "/" + destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();

		return dir + "/" + destinationFile;
	}

	private String getPictureName(String url) {

		int i = url.indexOf(".jpg");
		String suburl = url.substring(0, i + 4);
		String jpgName = url.substring(suburl.lastIndexOf("/") + 1, i + 4);
		// System.out.println("name : " + jpgName + "  ; url: " + url);
		return jpgName;
	}

	public static void main(String[] args) throws Exception {
		AnalysisDao anlDao = new AnalysisDao("localhost", "analysis", "uhoh",
				27017);
		
		UserDao userDao = new UserDao("localhost", "user", "uhoh",
				27017);
		AnalyzeService svc = new AnalyzeService(anlDao, userDao);
		UserObj user = new UserObj();
		user.setFbToken("CAACEdEose0cBAN98Ry0CIOLtTk0r3AbINx5RZChY5JwV6eINOyPnpxUQAMwMbPPWPfqd18J64f5iawJCU1UWaFvuZBU7KvNsTbZABXaqe8FuNtPH9VFcwrDberZAVKnulqTzQ7ZBr5XLi1l0BgWuwUsed8zMqvOmwYFZCOrgK633OrD1B2IDmxoXKULw7w1yNefdel6uRUIwZDZD");
		user.setLastChecked(0L);
		user.setUOId("56e084a2b57d1830cf1e7104");
		svc.analyzeUser(user);

	}
}
