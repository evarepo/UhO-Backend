package com.uhoh.uhohws.dto;

public class ToneNode{
	float score;
	String tone_id;
	String tone_name;
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public String getTone_id() {
		return tone_id;
	}
	public void setTone_id(String tone_id) {
		this.tone_id = tone_id;
	}
	public String getTone_name() {
		return tone_name;
	}
	public void setTone_name(String tone_name) {
		this.tone_name = tone_name;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "score:" + score + "; tone_id:" + tone_id + "; tone_name:" + tone_name ;
	}
}
