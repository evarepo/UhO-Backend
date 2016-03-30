package com.uhoh.uhohws.dto;

import java.util.ArrayList;

public class ImageNode {

	String image;
	ArrayList<ImageClassNode> scores;

	public ImageNode() {
		scores = new ArrayList<ImageClassNode>();
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public ArrayList<ImageClassNode> getScores() {
		return scores;
	}

	public void setScores(ArrayList<ImageClassNode> scores) {
		this.scores = scores;
	}

}
