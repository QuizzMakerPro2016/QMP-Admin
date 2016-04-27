package com.qmp.admin.utils.fieldchecker;

import java.util.ArrayList;
import java.util.List;

import com.qmp.admin.utils.Notifier;

import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

public class TextFieldChecker {
	
	private List<TextInputControl> fieldsToCheck;
	
	public TextFieldChecker(TextInputControl...fields){
		this.fieldsToCheck = new ArrayList<>();
		for(TextInputControl field : fields){
			this.fieldsToCheck.add(field);
		}
	}
	
	public boolean run(){
		for(TextInputControl tf : this.fieldsToCheck){
			if(tf.getText().isEmpty()){
				Notifier.notifyWarning("Champ vide !", "Veuillez renseigner le champ '" + tf.getPromptText() + "'.");
				return false;
			}
		}
		return true;
	} 
	

}
