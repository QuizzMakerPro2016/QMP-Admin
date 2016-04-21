package com.qmp.admin.controllers;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Domaine;
import com.qmp.admin.models.Questionnaire;
import com.qmp.admin.utils.GraphicUtils;

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
import javafx.scene.control.Alert.AlertType;

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
    public void setMainApp(MainApp mainApp) {
    	super.setMainApp(mainApp);
    	ObservableList<Domaine> lstDo = mainApp.getWebGate().getList(Domaine.class);
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

    	int selInxdex = tableDomainList.getSelectionModel().getSelectedIndex();
		Domaine selectedDomain = tableDomainList.getSelectionModel().getSelectedItem();
		if (selInxdex >= 0) {
			boolean response = gUtils.showDialog("Suppression", "Supprimer un domaine ?", "Voulez-vous vraiment supprimer le domaine '" + selectedDomain.getLibelle() + "' ?");
			if(response){
				tableDomainList.getItems().remove(selInxdex);
				try {
					mainApp.getTaskQueue().delete(selectedDomain, selectedDomain.getId());
				} catch (Exception e) {
					GraphicUtils.showException(e);
				}
			}
			
		} else {
			//error no selected
			//TODO create Bootstrap Like Alerts
		}
    }

    @FXML
    void handleNew(ActionEvent event) {
    	showDomain(null);
    	tableDomainList.getSelectionModel().clearSelection();
    }

    @FXML
    void handleSave(ActionEvent event) {
    	//Empty Fields Check
    	if(tfLibelle.getText().isEmpty()){
    		GraphicUtils.showAlert("Erreur", "Impossible d'enregitrer le domaine", "Veuillez renseigner le libellé du domaine", AlertType.ERROR);
    		return;
    	}
    	
    	int selInxdex = tableDomainList.getSelectionModel().getSelectedIndex();
		
		if (selInxdex >= 0) {
			//Update
			Domaine selectedDomain = tableDomainList.getSelectionModel().getSelectedItem();
			selectedDomain.setLibelle(tfLibelle.getText());
			try {
				mainApp.getWebGate().update(selectedDomain, selectedDomain.getId());
				mainApp.getTaskQueue().getAll(Domaine.class);
			} catch (Exception e) {
				GraphicUtils.showException(e);
			}
		} else {
			//Insertion
			Domaine domain = new Domaine();
			domain.setLibelle(tfLibelle.getText());
			
			try {
				String res = mainApp.getWebGate().add(domain);
				Domaine d = (Domaine) mainApp.getWebGate().getObjectFromJson(res, Domaine.class);
				mainApp.getWebGate().getList(Domaine.class).add(d);
				showDomain(d);
			} catch (Exception e) {
				GraphicUtils.showException(e);
			}
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

