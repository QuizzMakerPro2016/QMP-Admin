package com.qmp.admin.controllers;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Domaine;
import com.qmp.admin.models.Questionnaire;
import com.qmp.admin.utils.Notifier;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ManageDomainController extends Controller {

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
    
    @Override
    public void setMainApp(MainApp mainApp) throws ClientProtocolException, IOException {
    	super.setMainApp(mainApp);
    	ObservableList<Domaine> lstDo = mainApp.getWebGate().getList(Domaine.class);
		tableDomainList.setItems(lstDo);
    }

	@FXML
	private void initialize() {
		
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
		
		addFieldsToCheck(tfLibelle);
	}

    @FXML
    void handleDelete(ActionEvent event) {

    	int selInxdex = tableDomainList.getSelectionModel().getSelectedIndex();
		Domaine selectedDomain = tableDomainList.getSelectionModel().getSelectedItem();
		if (selInxdex >= 0) {
			boolean response = gUtils.showDialog("Suppression", "Supprimer un domaine ?", "Voulez-vous vraiment supprimer le domaine '" + selectedDomain.getLibelle() + "' ?");
			if(response){
				deleteObject(selectedDomain, selectedDomain.getId());
				tableDomainList.getItems().remove(selInxdex);
			}
		} else {
			Notifier.notifyWarning("Attention", "Aucun domaine selectionné.");
		}
		
    }

    @FXML
    void handleNew(ActionEvent event) {
    	showDomain(null);
    	tableDomainList.getSelectionModel().clearSelection();
    }

    @FXML
    void handleSave(ActionEvent event) {
    	
    	if(!checkFields())
    		return;
    	
    	Domaine domain = tableDomainList.getSelectionModel().getSelectedItem();
    	if(domain == null)
    		domain = new Domaine();
    	
    	if(Integer.valueOf(tfDomainID.getText()) > 0)
    		domain.setId(Integer.valueOf(tfDomainID.getText()));
    	
    	domain.setLibelle(tfLibelle.getText());
		
		if (domain.getId() > 0) {
			//Update
			domain = (Domaine) updateObject(domain, domain.getId());
		} else {
			//Add
			domain = (Domaine) addObject(domain);
		}
		if(domain != null){
			mainApp.getTaskQueue().getAll(Domaine.class);
			showDomain(domain);
		}
    }
    
    private void showDomain(Domaine domain){
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

