package com.qmp.admin.controllers;

import java.io.IOException;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Utilisateur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainController {
	
	@FXML
    private TextField loginField;
	@FXML
	private TextField passwordField;
	@FXML
	private Button connectButton;
    
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
    private void handleConnect(ActionEvent event) throws IOException {
		Utilisateur result = mainApp.getWebGate().connect(loginField.getText(), passwordField.getText());
		mainApp.setUser(result);
		if (mainApp.isLogged() == true){
			if(mainApp.isAdmin()){
				errorText.setText("Connection réussie de "+ mainApp.getUser().getPrenom() +" "+ mainApp.getUser().getNom());
				
		        Parent dispatcher = FXMLLoader.load(getClass().getResource("/com/qmp/admin/views/MainPage.fxml"));
		        Scene dispatcherScene = new Scene(dispatcher);
		        Stage appStage = (Stage)((Node) event.getSource()).getScene().getWindow();
		        appStage.hide(); //optional
                appStage.setScene(dispatcherScene);
                appStage.show();  			
				
			}else{
				errorText.setText("Cet utilisateur ne dispose pas des droits nécessaires ("+result.getRang().getLibelle()+")");
			}
		}else{
			errorText.setText("Connection échouée");
		}
    }
}
