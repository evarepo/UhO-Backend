package com.uhoh.uhohws.dto;

import java.util.ArrayList;

public class ToneElement{
	ArrayList<ToneNode> tones ;
	String category_id;
	String category_name;
	
	public ToneElement(){
		tones = new ArrayList<ToneNode>();
	}

	public ArrayList<ToneNode> getTones() {
		return tones;
	}

	public void setTones(ArrayList<ToneNode> tones) {
		this.tones = tones;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	
}
