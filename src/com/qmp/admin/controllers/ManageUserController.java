package com.qmp.admin.controllers;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Domaine;
import com.qmp.admin.models.Groupe;
import com.qmp.admin.models.Questionnaire;
import com.qmp.admin.models.Rang;
import com.qmp.admin.models.Utilisateur;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ManageUserController extends Controller {

    @FXML
    private TableView<Utilisateur> userList;

    @FXML
    private TableColumn<Utilisateur, String> surnameColumn;

    @FXML
    private TableColumn<Utilisateur, String> nameColumn;

    @FXML
    private TableColumn<Rang, String> rankColumn;

    @FXML
    private Button newButton;

    @FXML
    private TextField idField;
    
    @FXML
    private TextField surnameField;

    @FXML
    private TextField mailField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox<Rang> rankField;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private TableView<Groupe> groupList;

    @FXML
    private TableColumn<Groupe, String> groupListColumn;

    @FXML
    private TableView<Questionnaire> quizzList;

    @FXML
    private TableColumn<Questionnaire, String> quizzListColumn;
    
    @Override
    public void setMainApp(MainApp mainApp) {
    	super.setMainApp(mainApp);
    	ObservableList<Utilisateur> userObs = mainApp.getWebGate().getList(Utilisateur.class);
    	userList.setItems(userObs);
    }

    @FXML
	private void initialize() {
    		
		surnameColumn.setCellValueFactory((CellDataFeatures<Utilisateur, String> feature) -> {
			Utilisateur user = feature.getValue();
			return new SimpleObjectProperty<>(user.getNom());
		});
		
		nameColumn.setCellValueFactory((CellDataFeatures<Utilisateur, String> feature) -> {
			Utilisateur user = feature.getValue();
			return new SimpleObjectProperty<>(user.getNom());
		});
		
		rankColumn.setCellValueFactory((CellDataFeatures<Rang, String> feature) -> {
			Rang rang = feature.getValue();
			return new SimpleObjectProperty<>(rang.getLibelle());
		});
		
		quizzListColumn.setCellValueFactory((CellDataFeatures<Questionnaire, String> feature) -> {
			Questionnaire quizz = feature.getValue();
			return new SimpleObjectProperty<>(quizz.getLibelle());
		});
		
		groupListColumn.setCellValueFactory((CellDataFeatures<Groupe, String> feature) -> {
			Groupe group = feature.getValue();
			return new SimpleObjectProperty<>(group.getLibelle());
		});
		
		showUser(null);
		userList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showUser(newValue));
		
		
	}
    
    public void showUser(Utilisateur user){
    	
    	if(user == null){
    		idField.setText("0");
    		surnameField.setText("");
    		passwordField.setText("");
    		nameField.setText("");
    	}else{
    		idField.setText(String.valueOf(user.getId()));
    		surnameField.setText(user.getNom());
    		nameField.setText(user.getPrenom());
    		passwordField.setText("");
    		quizzList.setItems(FXCollections.observableArrayList(user.getQuestionnaires()));
    		groupList.setItems(FXCollections.observableArrayList(user.getGroupes()));
    	}
    }
}
