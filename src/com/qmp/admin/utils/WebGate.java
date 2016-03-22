package com.qmp.admin.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;

public class WebGate {
	private Map<String, String> tabCorr;
	private String baseUrl;
	private Gson gson;

	public WebGate() {
		baseUrl = "http://127.0.0.1:8080/QMP-Rest/rest/";
		tabCorr = new HashMap<>();
		tabCorr.put("Utilisateur", "user");
		tabCorr.put("Domaine", "domain");
		tabCorr.put("Groupe_utilisateur", "usergroup");
		tabCorr.put("Groupe", "group");
		tabCorr.put("Question", "question");
		tabCorr.put("Questionnaire", "quizz");
		tabCorr.put("Rang", "rank");
		tabCorr.put("Reponse", "answer");
		
		gson = MyGsonBuilder.create();
	}

	public <T> List<T> getAll(Class<T> clazz, int cd) throws ClientProtocolException, IOException {
		if(cd < 0) cd = 1;
		
		List<T> result = new ArrayList<T>();
		String jsonObjects = HttpUtils.getHTML(baseUrl + tabCorr.get(clazz.getSimpleName()) + "/all");
		result = gson.fromJson(jsonObjects, new ListType<T>(clazz));
		return result;
	}
	
	public <T> T getOne(Class<T> clazz, int id, int cd) throws ClientProtocolException, IOException {
		if(id < 0) return null;
		if(cd < 0) cd = 1;

		String jsonObject = HttpUtils.getHTML(baseUrl + tabCorr.get(clazz.getSimpleName()) + "/id/");
		T result = gson.fromJson(jsonObject, clazz.getClass());
		
		return result;
	}

}
