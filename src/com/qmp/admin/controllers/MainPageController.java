package com.qmp.admin.controllers;

import java.io.IOException;

import com.qmp.admin.MainApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainPageController {

    @FXML
    private Button userButton;

    @FXML
    private Text userText;
    
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public MainPageController(){
    	
    }
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }
    
    @FXML
    void handleUser(ActionEvent event) throws IOException{

    	Parent dispatcher = FXMLLoader.load(getClass().getResource("/com/qmp/admin/views/ManageUserLayout.fxml"));
        Scene dispatcherScene = new Scene(dispatcher);
        Stage appStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        appStage.hide(); //optional
        appStage.setScene(dispatcherScene);
        appStage.show(); 
        
    }
    
}

