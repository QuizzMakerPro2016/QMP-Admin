package com.qmp.admin.controllers;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;
import com.qmp.admin.MainApp;
import com.qmp.admin.models.Utilisateur;
import com.qmp.admin.utils.WebGate;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class MainController {
	
	@FXML
    private TextField loginField;
	@FXML
	private TextField passwordField;
    
	 // Reference to the main application.
    private MainApp mainApp;

    private Utilisateur user ;
    
    private WebGate webGate;
    
    public MainController(){
    	
    }
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    @FXML
    private void handleConnect() throws ClientProtocolException, IllegalArgumentException, IllegalAccessException, IOException {
    		String result = webGate.connect(loginField.getText(), passwordField.getText());
    		Gson gson = new Gson();
    		HashMap <String, String>resultHashMap = gson.fromJson(result, HashMap.class);
    		if(resultHashMap.get("message").contains("invalide"))
    			
    		user = webGate.getOne(Utilisateur.class,1);
    		this.mainApp.setUser(user);
    }
}
