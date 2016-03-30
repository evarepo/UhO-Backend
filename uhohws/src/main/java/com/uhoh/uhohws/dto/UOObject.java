package com.uhoh.uhohws.dto;

public class UOObject {

	String type = ""; // POST, COMMENT
	String postId = "";
	String postOwnerId = "";
	String postOwnerName = "";
	String postMsg = "";
	String postPic = "";
	String commentId = "";
	String commentOwnerName = "";
	String commentOwnerId = "";
	String commentMsg = "";

	public UOObject() {
		// emtpy constructor
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof UOObject) {
			UOObject tObj = (UOObject) obj;

			if (type.equals(tObj.getType()) && postId.equals(tObj.getPostId())
					&& postOwnerId.equals(tObj.getPostOwnerId())
					&& postPic.equals(tObj.getPostPic())
					&& commentId.equals(tObj.getCommentId())
					&& commentOwnerId.equals(tObj.getCommentOwnerId())
					&& commentMsg.equals(tObj.getCommentMsg()))
				return true;
			else
				return false;
		} else
			return false;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getPostOwnerId() {
		return postOwnerId;
	}

	public void setPostOwnerId(String postOwnerId) {
		this.postOwnerId = postOwnerId;
	}

	public String getPostMsg() {
		return postMsg;
	}

	public void setPostMsg(String postMsg) {
		this.postMsg = postMsg;
	}

	public String getPostPic() {
		return postPic;
	}

	public void setPostPic(String postPic) {
		this.postPic = postPic;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getCommentOwnerId() {
		return commentOwnerId;
	}

	public void setCommentOwnerId(String commentOwnerId) {
		this.commentOwnerId = commentOwnerId;
	}

	public String getCommentMsg() {
		return commentMsg;
	}

	public void setCommentMsg(String commentMsg) {
		this.commentMsg = commentMsg;
	}

	public String getPostOwnerName() {
		return postOwnerName;
	}

	public void setPostOwnerName(String postOwnerName) {
		this.postOwnerName = postOwnerName;
	}

	public String getCommentOwnerName() {
		return commentOwnerName;
	}

	public void setCommentOwnerName(String commentOwnerName) {
		this.commentOwnerName = commentOwnerName;
	}

}
