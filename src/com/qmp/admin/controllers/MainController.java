package com.qmp.admin.controllers;

import com.qmp.admin.MainApp;

import javafx.fxml.FXML;

public class MainController {
	
	 // Reference to the main application.
    private MainApp mainApp;

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
    
}
