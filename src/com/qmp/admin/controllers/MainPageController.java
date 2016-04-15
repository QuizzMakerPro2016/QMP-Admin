package com.qmp.admin.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class MainPageController extends Controller {

    @FXML
    private Button userButton;
    
    @FXML
    private Button domainButton;
    
    @FXML
    private Button rankButton;

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
    void handleRank(ActionEvent event) throws IOException{
    	gUtils.switchView("ManageRankLayout");
    }
    
    @FXML
    void handleDomain(ActionEvent event) throws IOException{
    	gUtils.switchView("ManageDomainLayout");
    }
    
    @FXML
    void handleGroup(ActionEvent event) throws IOException{
    	gUtils.switchView("ManageGroupLayout");
    }
    
}

