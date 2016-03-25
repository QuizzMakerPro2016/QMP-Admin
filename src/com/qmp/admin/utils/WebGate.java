package com.qmp.admin.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;

public class WebGate {
	private Map<String, String> restUrlMappings;
	private String baseUrl;
	private Gson gson;
	
	private HashMap<String, Object> data;
	private HashMap<String, Object> lastGets;


	public WebGate() {
		baseUrl = "http://127.0.0.1:8080/QMP-Rest/rest/";
		
		data = new HashMap<String, Object>();
		lastGets = new HashMap<String, Object>();
		
		restUrlMappings = new HashMap<String, String>();
		restUrlMappings.put("Utilisateur", "user");
		restUrlMappings.put("Domaine", "domain");
		restUrlMappings.put("Groupe_utilisateur", "usergroup");
		restUrlMappings.put("Groupe", "group");
		restUrlMappings.put("Question", "question");
		restUrlMappings.put("Questionnaire", "quizz");
		restUrlMappings.put("Rang", "rank");
		restUrlMappings.put("Reponse", "answer");
		
		gson = MyGsonBuilder.create();
	}

	private <T> String getControllerUrl(Class<T> clazz) {
		String result = clazz.getSimpleName();
		if (restUrlMappings.containsKey(result))
			result = restUrlMappings.get(clazz.getSimpleName());
		return result;
	}

	private Map<String, Object> beanToMap(Object o) {
		Map<String, Object> result = new HashMap<>();
		Field[] declaredFields = o.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.getType().isPrimitive() || PrimitiveTypes.isWrapperType(field.getType())) {
				field.setAccessible(true);
				System.out.println(field.getName());
				try {
					result.put(field.getName(), field.get(o));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;

	}

	public <T> List<T> getAll(Class<T> clazz) throws ClientProtocolException, IOException {
		List<T> result = new ArrayList<T>();
		
		long date = 0 ;
		if(data.get(getControllerUrl(clazz)) != null ){
			date = (long) lastGets.get(getControllerUrl(clazz));
			lastGets.remove(getControllerUrl(clazz));
		}
		
		lastGets.put(getControllerUrl(clazz), new Date().getTime());
		
		String jsonUsers = HttpUtils.getHTML(baseUrl + getControllerUrl(clazz) + "/modif/" + date);
		
		if(jsonUsers.equals("false"))
				return (List<T>) data.get(getControllerUrl(clazz));
		
		result = gson.fromJson(jsonUsers, new ListType<T>(clazz));
		
		if(data.get(getControllerUrl(clazz)) != null )
			data.remove((getControllerUrl(clazz)));
		
		data.put(getControllerUrl(clazz), result);
		
		return result;
	}

	public <T> T getOne(Class<T> clazz, Object id) throws ClientProtocolException, IOException {
		String jsonO = HttpUtils.getHTML(baseUrl + getControllerUrl(clazz) + "/" + id);
		T result = gson.fromJson(jsonO, clazz);
		return result;
	}

	public <T> String delete(T object, Object id) throws ClientProtocolException, IOException {
		return HttpUtils.deleteHTML(baseUrl + getControllerUrl(object.getClass()) + "/" + String.valueOf(id));
	}

	public <T> String add(T object) throws ClientProtocolException, IllegalArgumentException, IllegalAccessException, IOException {
		return HttpUtils.putHTML(baseUrl + getControllerUrl(object.getClass()) + "/add", beanToMap(object));
	}

	public <T> String update(T object, Object id) throws ClientProtocolException, IllegalArgumentException, IllegalAccessException, IOException {
		return HttpUtils.postHTML(baseUrl + getControllerUrl(object.getClass()) + "/update/" + id, beanToMap(object));
	}

}
