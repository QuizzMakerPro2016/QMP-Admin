package com.qmp.admin.controllers;

import java.util.Optional;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Domaine;
import com.qmp.admin.models.Questionnaire;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ManageDomainController {

	@FXML
    private TableView<Domaine> tableDomainList;

    @FXML
    private TableColumn<Domaine, String> tableDomainListColumn;

    @FXML
    private Button btnNew;

    @FXML
    private TextField tfLibelle;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Label tfDomainID;

    @FXML
    private TableView<Questionnaire> tableQuizzesList;
    
    @FXML
    private TableColumn<Questionnaire, String> tableQuizzesListCol;
    
    private MainApp mainApp;
    
    public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		ObservableList<Domaine> lstDo=mainApp.getWebGate().getList(Domaine.class);
		tableDomainList.setItems(lstDo);
	}
    

	@FXML
	private void initialize() {
		// Initialize the person table with the two columns.
		tableDomainListColumn.setCellValueFactory((CellDataFeatures<Domaine, String> feature) -> {
			Domaine domain = feature.getValue();
			return new SimpleObjectProperty<>(domain.getLibelle());
		});
		
		tableQuizzesListCol.setCellValueFactory((CellDataFeatures<Questionnaire, String> feature) -> {
			Questionnaire quizz = feature.getValue();
			return new SimpleObjectProperty<>(quizz.getLibelle());
		});
		
		showDomain(null);
		tableDomainList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showDomain(newValue));
	}

    @FXML
    void handleDelete(ActionEvent event) {
    	//if(questionnaires > 0)
    	//Ajouter dans le controller le currentDoamin
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Suppression de domaine");
    	alert.setHeaderText("Look, a Confirmation Dialog");
    	alert.setContentText("Are you ok with this?");

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK){
    		Alert alert2 = new Alert(AlertType.INFORMATION);
        	alert2.setTitle("Information Dialog");
            alert2.setContentText("OK");
            alert2.showAndWait();
    	} else {
    		Alert alert3 = new Alert(AlertType.INFORMATION);
        	alert3.setTitle("Information Dialog");
            alert3.setContentText("NOPE");
            alert3.showAndWait();
        	
    	}
    }

    @FXML
    void handleNew(ActionEvent event) {
    	showDomain(null);
    	tableDomainList.getSelectionModel().clearSelection();
    }

    @FXML
    void handleSave(ActionEvent event) {
    	//TODO
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Information Dialog");

    	if(Integer.valueOf(tfDomainID.getText()) > 0){
        	alert.setContentText("Domain Update : " + tfDomainID.getText());
    	}else{
        	alert.setContentText("New Domain");
    	}
    	alert.showAndWait();
    }
    
    public void showDomain(Domaine domain){
    	if(domain == null){
    		tfDomainID.setText("0");
    		tfLibelle.setText("");
    	}else{
    		tfDomainID.setText(String.valueOf(domain.getId()));
    		tfLibelle.setText(domain.getLibelle());
    		tableQuizzesList.setItems(FXCollections.observableArrayList(domain.getQuestionnaires()));
    	}
    }

}

