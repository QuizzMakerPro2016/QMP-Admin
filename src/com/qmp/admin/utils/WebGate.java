package com.qmp.admin.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qmp.admin.models.Utilisateur;

import javafx.collections.ObservableList;

public class WebGate {
	private Map<String, String> restUrlMappings;
	private Map<Class<? extends Object>, WebGateList> modelLists;
	private String baseUrl;

	public WebGate() {
		modelLists = new HashMap<>();
		baseUrl = "http://127.0.0.1:8080/QMP-Rest/rest/";
		restUrlMappings = new HashMap<>();
		restUrlMappings.put("Utilisateur", "user");
		restUrlMappings.put("Questionnaire", "quizz");
		restUrlMappings.put("Question", "question");
		restUrlMappings.put("Reponse", "answer");
		restUrlMappings.put("Domaine", "domain");
		restUrlMappings.put("Rang", "rank");
		restUrlMappings.put("Groupe", "group");
		restUrlMappings.put("Groupe_questionnaire", "quizzgroup");
		restUrlMappings.put("Groupe_utilisiateur", "usergroup");
		restUrlMappings.put("Question_questionnaire", "questquizz");
	}

	private <T> String getControllerUrl(Class<T> clazz) {
		String result = clazz.getSimpleName();
		if (restUrlMappings.containsKey(result))
			result = restUrlMappings.get(clazz.getSimpleName());
		return result;
	}
	
	public <T> Object getObjectFromJson(String json, Class<T> clazz){
		return getObjectFromJson(json, getControllerUrl(clazz), clazz);
	}
	
	public <T> Object getObjectFromJson(String json, String jsonElement, Class<T> clazz){
	    Gson gson = MyGsonBuilder.create();
		Object o = null;
		JsonObject jso = gson.fromJson(json, JsonObject.class);
		if (jso.get("error") == null) {
			o = gson.fromJson(jso.get(jsonElement), clazz);
		}
		return o;
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

	public Utilisateur connect(String login, String password) {
		Utilisateur user = null;
		Map<String, Object> params = new HashMap<>();
		params.put("login", login);
		params.put("password", password);
		try {
			String jsonString = HttpUtils.postHTML(baseUrl + getControllerUrl(Utilisateur.class) + "/connect", params);
			Gson gson = MyGsonBuilder.create();
			JsonObject jso = gson.fromJson(jsonString, JsonObject.class);
			if (jso.get("error") == null) {
				user = gson.fromJson(jso.get("utilisateur"), Utilisateur.class);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	public <T> List<T> getAll(Class<T> clazz) throws ClientProtocolException, IOException {
		List<T> result = new ArrayList<T>();
		String jsonUsers = HttpUtils.getHTML(baseUrl + getControllerUrl(clazz) + "/all/1");
		Gson gson = MyGsonBuilder.create();
		result = gson.fromJson(jsonUsers, new ListType<T>(clazz));
		return result;
	}

	public <T> List<T> getAll(Class<T> clazz, int offset, int limit, int cd) throws ClientProtocolException, IOException {
		List<T> result = new ArrayList<T>();
		String jsonUsers = HttpUtils.getHTML(baseUrl + getControllerUrl(clazz) + "/limit/" + offset + "/" + limit+"/"+String.valueOf(cd));
		Gson gson = MyGsonBuilder.create();
		result = gson.fromJson(jsonUsers, new ListType<T>(clazz));
		return result;
	}
	
	public <T> List<T> getAll(Class<T> clazz, int offset, int limit) throws ClientProtocolException, IOException {
		return getAll(clazz, offset, limit, 1);
	}

	public <T> T getOne(Class<T> clazz, Object id) throws ClientProtocolException, IOException {
		String jsonO = HttpUtils.getHTML(baseUrl + getControllerUrl(clazz) + "/" + id);
		Gson gson = MyGsonBuilder.create();
		T result = gson.fromJson(jsonO, clazz);
		return result;
	}
	
	public <T> List<T> getMembers(Class clazz, Object id, String member, Class<T> memberClass) throws ClientProtocolException, IOException {
		return getMembers(clazz, id, member, memberClass, 1);
	}
	
	public <T> List<T> getMembers(Class clazz, Object id, String member, Class<T> memberClass, int cd) throws ClientProtocolException, IOException {
		List<T> result = new ArrayList<T>();
		String jsonUsers = HttpUtils.getHTML(baseUrl + getControllerUrl(clazz) + "/" + id + "/all/" + member+"/"+String.valueOf(cd));
		Gson gson = MyGsonBuilder.create();
		result = gson.fromJson(jsonUsers, new ListType<T>(memberClass));
		return result;
	}

	public <T> String delete(T object, Object id) throws ClientProtocolException, IOException {
		return HttpUtils.deleteHTML(baseUrl + getControllerUrl(object.getClass()) + "/" + String.valueOf(id));
	}
	
	@SuppressWarnings("unchecked")
	public String deleteRelation(Class clazz, Object id1, Object id2) throws ClientProtocolException, IOException {
		return HttpUtils.deleteHTML(baseUrl + getControllerUrl(clazz) + "/" + String.valueOf(id1) + "/" + String.valueOf(id2));
	}

	public <T> String add(T object) throws ClientProtocolException, IllegalArgumentException, IllegalAccessException, IOException {
		return HttpUtils.putHTML(baseUrl + getControllerUrl(object.getClass()) + "/add", beanToMap(object));
	}

	public <T> String update(T object, Object id) throws ClientProtocolException, IllegalArgumentException, IllegalAccessException, IOException {
		return HttpUtils.postHTML(baseUrl + getControllerUrl(object.getClass()) + "/update/" + id, beanToMap(object));
	}

	public <T> int count(Class<T> clazz) throws ClientProtocolException, IOException {
		String jsonO = HttpUtils.getHTML(baseUrl + getControllerUrl(clazz) + "/count");
		Gson gson = MyGsonBuilder.create();
		int result = gson.fromJson(jsonO, Integer.class);
		return result;
	}

	public <T> boolean isModified(Class<T> clazz) throws ClientProtocolException, IOException {
		if (!modelLists.containsKey(clazz))
			return true;
		WebGateList wgList = modelLists.get(clazz);
		if (wgList.getTimestamp() == null)
			return true;
		String jsonO = HttpUtils.getHTML(baseUrl + getControllerUrl(clazz) + "/modif/" + wgList.getTimestamp());
		Gson gson = MyGsonBuilder.create();
		boolean result = gson.fromJson(jsonO, Boolean.class);
		return result;
	}
	
	public boolean doRelationExists(Class clazz, int idOne, int idTwo) throws ClientProtocolException, IOException {
		String jsonO = HttpUtils.getHTML(baseUrl + getControllerUrl(clazz) + "/exist/" + String.valueOf(idOne) + "/" + String.valueOf(idTwo));
		Gson gson = MyGsonBuilder.create();
		boolean result = gson.fromJson(jsonO, Boolean.class);
		return result;
	}

	public <T> WebGateList getWebGateList(Class<T> clazz) {
		if (!modelLists.containsKey(clazz))
			modelLists.put(clazz, new WebGateList());
		return modelLists.get(clazz);
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> ObservableList<T> getList(Class<T> clazz) {
		if (!modelLists.containsKey(clazz))
			modelLists.put(clazz, new WebGateList());
		return (ObservableList<T>) modelLists.get(clazz).getList();
	}

	public <T extends Object> boolean add(T o, Class<T> clazz) {
		return getList(clazz).add(o);
	}

	public boolean addAll(List<Object> list, Class<Object> clazz) {
		return getList(clazz).addAll(list);
	}

	public void clearList(Class<Object> clazz) {
		getList(clazz).clear();
	}


}
