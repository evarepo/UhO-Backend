package com.uhoh.uhohws.dto;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.uhoh.uhohws.service.AnalyzeService.ObjectAnalysis;

public class AnalysisResponse {
	Map<String, ArrayList<ObjectAnalysis>> negMap ;
	int totalPosts;
	
	public AnalysisResponse(Map<String, ArrayList<ObjectAnalysis>> negMap,
			int totalPosts) {
		super();
		this.negMap = negMap;
		this.totalPosts = totalPosts;
	}
	
	
}
