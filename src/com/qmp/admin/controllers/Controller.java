package com.qmp.admin.controllers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qmp.admin.MainApp;
import com.qmp.admin.models.Domaine;
import com.qmp.admin.models.Reponse;
import com.qmp.admin.models.Utilisateur;
import com.qmp.admin.utils.GraphicUtils;
import com.qmp.admin.utils.MyGsonBuilder;
import com.qmp.admin.utils.Notifier;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class Controller {

	protected MainApp mainApp;
	protected GraphicUtils gUtils;
	
	private List<TextField> fieldsToCheck;

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		this.gUtils = new GraphicUtils(mainApp);
	}
	
	protected void addFieldsToCheck(TextField... fields){
		if(this.fieldsToCheck == null)
			this.fieldsToCheck = new ArrayList<>();
		for(TextField field : fields){
			this.fieldsToCheck.add(field);
		}
	}

	/**
	 * Permet d'automatiquement créer le Filtre correspondant à une TableView et
	 * un champs texte
	 * 
	 * @param search
	 *            Le champ texte
	 * @param liste
	 *            La tableview à filtrer
	 * @param obs
	 *            La liste des données
	 * @param fields
	 *            la liste des champs où faire la recherche ex: "nom", "prenom"
	 *            pour une personne
	 * @return void
	 **/
	protected void setFilterTableView(TextField search, TableView liste, ObservableList obs, ArrayList fields) {
		// Permet de faire en sorte que la liste soit filtrable
		FilteredList<Object> filteredData = new FilteredList<>(obs, p -> true);

		// Ajoute un listener pour vérifier tout changement sur le champs texte
		search.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(feature -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name of every person with filter
				// text.
				String lowerCaseFilter = newValue.toLowerCase();

				for (Object obj : fields) {
					try {
						Field var = feature.getClass().getDeclaredField(obj.toString());
						var.setAccessible(true);

						if (var.get(feature).toString().toLowerCase().contains(lowerCaseFilter)) {
							return true; // Filter matches first name.
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return false; // Does not match.
			});
		});

		// La sorted list permet de trier les résultats obtenus
		SortedList<Object> sortedData = new SortedList<>(filteredData);

		// Bind la recherche et les données.
		sortedData.comparatorProperty().bind(liste.comparatorProperty());

		// Add sorted (and filtered) data to the table.
		liste.setItems(sortedData);
	}
	
	protected boolean checkFields(){
		
		for(TextField tf : this.fieldsToCheck){
			if(tf.getText().isEmpty()){
				Notifier.notifyWarning("Champ vide !", "Veuillez renseigner le champ '" + tf.getPromptText() + "'.");
				return false;
			}
		}
		return true;
	}
	
	protected  <T> Object checkResult(Class<T> clazz, String json, String successMsg){
		T obj = (T) mainApp.getWebGate().getObjectFromJson(json, clazz);
		if(obj != null){
			Notifier.notifySuccess("Succès", successMsg.replace("{{object}}", obj.toString()));
			return obj;
		}
		Gson gson = MyGsonBuilder.create();
		JsonObject jso = gson.fromJson(json, JsonObject.class);
		if (jso.get("error") != null) {
			Notifier.notifyError("Erreur", ((JsonObject) jso.get("error")).get("message").toString());
		}
		return null;
	}
	
	protected Object addObject(Object object){
		Object o = null;
		try {
			String res = mainApp.getWebGate().add(object);
			o = checkResult(object.getClass(), res, object.getClass().getSimpleName() + " '{{object}}' ajouté.");
		} catch (Exception e) {
			GraphicUtils.showException(e);
		}
		return o;
	}
	
	protected Object updateObject(Object object, int objId){
		Object o = null;
		try {
			String res = mainApp.getWebGate().update(object, objId);
			o = checkResult(object.getClass(), res, object.getClass().getSimpleName() + " '{{object}}' mis à jour.");
		} catch (Exception e) {
			GraphicUtils.showException(e);
		}
		return o;
	}
	
	protected boolean deleteObject(Object object, int objectId){
		Object o = null;
		try {
			String res = mainApp.getWebGate().delete(object, objectId);
			o = checkResult(object.getClass(), res, object.getClass().getSimpleName() + " '{{object}}' supprimé.");
		} catch (Exception e) {
			GraphicUtils.showException(e);
		}
		if(o == null)
			return false;
		return true;
	}

}
