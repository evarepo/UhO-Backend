package com.uhoh.uhohws.dto;

import java.util.HashMap;
import java.util.Map;

public class UserAnalysis {

	String userId;
	int totalPosts=0;
	int posSplit=0;
	int negSplit=0;
	int negCommentsSplit=0;
	int negPhotosSplit=0;
	int negVideoSplit=0;
	
	Map<String, PostAnalysis> analysis;
	
	public UserAnalysis(){
		analysis = new HashMap<String, PostAnalysis>();
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Map<String, PostAnalysis> getAnalysis() {
		return analysis;
	}
	public void setAnalysis(Map<String, PostAnalysis> analysis) {
		this.analysis = analysis;
	}
	
	public void addPostToAnalysis(String postId, PostAnalysis pa){
		analysis.put(postId, pa);
	}

	public int getPosSplit() {
		return posSplit;
	}

	public void setPosSplit(int posSplit) {
		this.posSplit = posSplit;
	}

	public int getNegSplit() {
		return negSplit;
	}

	public void setNegSplit(int negSplit) {
		this.negSplit = negSplit;
	}

	public int getNegCommentsSplit() {
		return negCommentsSplit;
	}

	public void setNegCommentsSplit(int negCommentsSplit) {
		this.negCommentsSplit = negCommentsSplit;
	}

	public int getNegPhotosSplit() {
		return negPhotosSplit;
	}

	public void setNegPhotosSplit(int negPhotosSplit) {
		this.negPhotosSplit = negPhotosSplit;
	}

	public int getNegVideoSplit() {
		return negVideoSplit;
	}

	public void setNegVideoSplit(int negVideoSplit) {
		this.negVideoSplit = negVideoSplit;
	}

	public int getTotalPosts() {
		return totalPosts;
	}

	public void setTotalPosts(int totalPosts) {
		this.totalPosts = totalPosts;
	}
	
	
}
