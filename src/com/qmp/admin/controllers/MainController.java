package com.qmp.admin.controllers;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Utilisateur;
import com.qmp.admin.utils.WebGate;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class MainController {
	
	@FXML
    private TextField loginField;
	@FXML
	private TextField passwordField;
    
	@FXML
	private Text errorText;
	
	 // Reference to the main application.
    private MainApp mainApp;

    private Utilisateur user ;
    
   
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
    private void handleConnect()  {
    		Utilisateur result = mainApp.getWebGate().connect(loginField.getText(), passwordField.getText());
    		if (result != null){
    			errorText.setText("Connection réussie de "+ result.getPrenom() +" "+ result.getNom());
    		}else{
    			errorText.setText("Connection échouée");
    		}
    }
}
