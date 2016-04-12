package com.qmp.admin.controllers;

import java.io.IOException;

import com.qmp.admin.MainApp;
import com.qmp.admin.utils.GraphicUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainPageController extends Controller {

    @FXML
    private Button userButton;
    
    @FXML
    private Button domainButton;

    @FXML
    private Text userText;    
    
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
    	gUtils.switchView("ManageUserLayout");
    }
    
    @FXML
    void handleDomain(ActionEvent event) throws IOException{
    	gUtils.switchView("ManageDomaisssnLayout");
    }
    
}

