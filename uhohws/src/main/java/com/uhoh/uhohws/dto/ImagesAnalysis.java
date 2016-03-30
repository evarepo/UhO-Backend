package com.uhoh.uhohws.dto;

import java.util.ArrayList;

public class ImagesAnalysis {

	ArrayList<ImageNode> images;
	
	public ImagesAnalysis(){
		images = new ArrayList<ImageNode>();
	}

	public ArrayList<ImageNode> getImages() {
		return images;
	}

	public void setImages(ArrayList<ImageNode> images) {
		this.images = images;
	}
	
	

}
