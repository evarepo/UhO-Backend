package com.uhoh.uhohws.dto;

import java.util.ArrayList;

public class PostAnalysis {

	public static class Comment {
		String commentId;
		String observation;

		public Comment(String commentId, String observation) {
			this.commentId = commentId;
			this.observation = observation;
		}

		public String getCommentId() {
			return commentId;
		}

		public void setCommentId(String commentId) {
			this.commentId = commentId;
		}

		public String getObservation() {
			return observation;
		}

		public void setObservation(String observation) {
			this.observation = observation;
		}

	}

	String postId="";
	String picURL="";
	String picComment="";
	ArrayList<Comment> negCmnts;
	String postMsg="";
	String postMsgComment="";
	boolean thisIsOk = false;

	
	public PostAnalysis() {
		negCmnts = new ArrayList<Comment>(); 
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getPicURL() {
		return picURL;
	}

	public void setPicURL(String picURL) {
		this.picURL = picURL;
	}

	public String getPicComment() {
		return picComment;
	}

	public void setPicComment(String picComment) {
		this.picComment = picComment;
	}

	public ArrayList<Comment> getNegCmnts() {
		return negCmnts;
	}

	public void setNegCmnts(ArrayList<Comment> negCmnts) {
		this.negCmnts = negCmnts;
	}

	public void addComment(String commentId, String observation){
		negCmnts.add(new Comment(commentId, observation));
	}

	public String getPostMsg() {
		return postMsg;
	}

	public void setPostMsg(String postMsg) {
		this.postMsg = postMsg;
	}

	public String getPostMsgComment() {
		return postMsgComment;
	}

	public void setPostMsgComment(String postMsgComment) {
		this.postMsgComment = postMsgComment;
	}

	public boolean isThisIsOk() {
		return thisIsOk;
	}

	public void setThisIsOk(boolean thisIsOk) {
		this.thisIsOk = thisIsOk;
	}
	
}
