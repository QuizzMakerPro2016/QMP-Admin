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

	public WebGate() {
		baseUrl = "http://127.0.0.1:8080/Quiz-Rest/rest/";
		tabCorr = new HashMap<>();
		tabCorr.put("Utilisateur", "user");
	}

	public <T> List<T> getAll(Class<T> clazz) throws ClientProtocolException, IOException {
		List<T> result = new ArrayList<T>();
		String jsonUsers = HttpUtils.getHTML(baseUrl + tabCorr.get(clazz.getSimpleName()) + "/all");
		Gson gson = MyGsonBuilder.create();
		result = gson.fromJson(jsonUsers, new ListType<T>(clazz));
		return result;
	}

}
