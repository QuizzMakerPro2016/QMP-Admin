package com.qmp.admin.utils.fieldchecker;

import java.util.ArrayList;
import java.util.List;

import com.qmp.admin.utils.Notifier;

import javafx.scene.control.TextInputControl;

public class TextFieldChecker {
	
	private List<TextInputControl> fieldsToCheck;
	private static String required = " (Requis)";
	
	public TextFieldChecker(TextInputControl...fields){
		this.fieldsToCheck = new ArrayList<>();
		addAll(fields);
	}
	
	public boolean run(){
		List<TextInputControl> emptyFields = new ArrayList<>();
		for(TextInputControl tf : this.fieldsToCheck){
			if(tf.getText().isEmpty()){
				emptyFields.add(tf);
				tf.setStyle("-fx-prompt-text-fill: red;");
			}else{
				tf.setStyle("-fx-prompt-text-fill: grey;");
			}
		}
		
		if(emptyFields.isEmpty())
			return true;

		if(emptyFields.size() > 1){
			Notifier.notifyWarning("Champs vides !", "Veuillez renseigner les champs requis.");
		}else{
			Notifier.notifyWarning("Champ vide !", "Veuillez renseigner le champ '" + emptyFields.get(0).getPromptText().replace(required, "") + "'.");
		}
		return false;
	}
	
	public void add(TextInputControl field){
		if(!fieldsToCheck.contains(field)){
			fieldsToCheck.add(field);
			field.setPromptText(field.getPromptText() + required );
		}
	}
	
	public void addAll(TextInputControl... fields){
		for(TextInputControl field : fields){
			add(field);
		}
	}
	
	public List<TextInputControl> getFieldsToCheck(){
		return this.fieldsToCheck;
	}
	

}
