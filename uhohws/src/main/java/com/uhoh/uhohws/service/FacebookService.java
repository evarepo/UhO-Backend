package com.uhoh.uhohws.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Comment;
import com.restfb.types.Comments;
import com.restfb.types.Photo;
import com.restfb.types.Post;
import com.restfb.types.User;
import com.uhoh.uhohws.conf.UOConstants;
import com.uhoh.uhohws.dto.UOObject;

public class FacebookService {

	private String accessToken = "CAACEdEose0cBAPulQFIvmcFZAbrw7rHXmvIHpUaei4rUXfnb6jZB8q1jCNvWRf9sjZAEc1As30T5IyQR6yypO9caLZCkAQuETF5UmvvMQ1qiOfI5Ntnpgqp9NBWPZBknogGqSBx7GwJb276xMzfPILrMdgMUCKkOWd9KAZBO1PgmqCl6Kfe52vnZB7UUZAshYaLZCtOZCNlxwmbQZDZD";
	private FacebookClient facebookClient = null;

	@SuppressWarnings("deprecation")
	public FacebookService(String accessToken) {
		if (accessToken == null || accessToken.length() == 0) {
			accessToken = this.accessToken;
		}
		facebookClient = new DefaultFacebookClient(accessToken);
	}

	public String getMyId(String token) {
		User user = facebookClient.fetchObject("me", User.class);
		return user.getId();
	}

	private void parseFeed(Long lastChecked, ArrayList<UOObject> arr,
			String userId) {
		Connection<Post> myFeed = facebookClient.fetchConnection("me/feed",
				Post.class,
				Parameter.with("fields", "message,full_picture,comments,from"),
				Parameter.with("since", new Date(lastChecked)));
		for (List<Post> myFeedConnectionPage : myFeed) {
			for (Post post : myFeedConnectionPage) {
				// populate post details here
				//System.out.println("post:"  + post.getFullPicture());
				UOObject obj = new UOObject();
				obj.setType(UOConstants.POST_TYPE);
				obj.setPostId(post.getId());
				obj.setPostMsg(post.getMessage());
				obj.setPostPic(post.getFullPicture());
				obj.setPostOwnerId(post.getFrom().getId());
				obj.setPostOwnerName(post.getFrom().getName());
				if (post.getFrom().getId().equals(userId))
					arr.add(obj);

				Comments comments = post.getComments();
				if (comments == null)
					continue;
				for (Comment comment : comments.getData()) {
					UOObject cmnt = new UOObject();
					cmnt.setType(UOConstants.COMMENT_TYPE);
					cmnt.setPostId(post.getId());
					cmnt.setCommentId(comment.getId());
					cmnt.setCommentMsg(comment.getMessage());
					cmnt.setCommentOwnerId(comment.getFrom().getId());
					cmnt.setCommentOwnerName(comment.getFrom().getName());
					if (comment.getFrom().getId().equals(userId))
						arr.add(cmnt);
					else {
						// System.out.println("someone else's comment: " +
						// comment.getMessage());
					}
				}
			}
		}

	}

	private void getPics(Long lastChecked, ArrayList<UOObject> arr,
			String userId) {

		Connection<Photo> myPics = facebookClient.fetchConnection(
				"me/photos/uploaded", Photo.class,
				Parameter.with("limit", 1000),
				Parameter.with("since", new Date(lastChecked)));

		for (Photo p : myPics.getData()) {
			System.out.println("pic:"  + p.getSource());
			UOObject obj = new UOObject();
			obj.setType(UOConstants.PHOTO_TYPE);
			obj.setPostId(p.getId());
			obj.setPostPic(p.getSource());
			obj.setPostOwnerId(p.getFrom().getId());
			obj.setPostOwnerName(p.getFrom().getName());
			if (p.getFrom().getId().equals(userId))
				arr.add(obj);
		}
		System.out.println("no .of photos: " + arr.size());
		Connection<Photo> picsofMe = facebookClient.fetchConnection(
				"me/photos", Photo.class, Parameter.with("limit", 1000),
				Parameter.with("since", new Date(lastChecked)));

		for (Photo p : picsofMe.getData()) {
			System.out.println(p.getSource());
			UOObject obj = new UOObject();
			obj.setType(UOConstants.PHOTO_TYPE);
			obj.setPostId(p.getId());
			obj.setPostPic(p.getSource());
			obj.setPostOwnerId(p.getFrom().getId());
			obj.setPostOwnerName(p.getFrom().getName());
			arr.add(obj);
		}

		System.out.println("no .of photos: " + arr.size());

		// ///////////////
		System.out.println("done crawling pics for user: " + userId);

	}

	public ArrayList<UOObject> getContentsOfUser(Long lastChecked) {
		User user = facebookClient.fetchObject("me", User.class);
		System.out.println("Me = " + user.getId()); // 1506636821

		ArrayList<UOObject> arr = new ArrayList<UOObject>();

		parseFeed(lastChecked, arr, user.getId());
		//getPics(lastChecked, arr, user.getId());

		System.out.println("size: " + arr.size());
		return arr;
	}

	public static void main(String[] args) {

		FacebookService svc = new FacebookService("");
		svc.getContentsOfUser(0L);

	}
}
