package com.uhoh.uhohws.dto;

import java.util.ArrayList;

public class DocumentTone{
	ArrayList<ToneElement> tone_categories;
	
	public DocumentTone(){
		tone_categories = new ArrayList<ToneElement>();
	}

	public ArrayList<ToneElement> getTone_categories() {
		return tone_categories;
	}

	public void setTone_categories(ArrayList<ToneElement> tone_categories) {
		this.tone_categories = tone_categories;
	}
	
	
}