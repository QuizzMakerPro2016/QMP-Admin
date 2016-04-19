package com.qmp.admin.controllers;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Domaine;
import com.qmp.admin.models.Questionnaire;
import com.qmp.admin.models.Rang;
import com.qmp.admin.models.Utilisateur;
import com.sun.media.jfxmedia.logging.Logger;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;

public class QuizzHomeController extends Controller{
	@FXML
	private TableView<Questionnaire> tableQuizzList;
	@FXML
	private TableColumn<Questionnaire, String> nameColumn;
	@FXML
	private TableColumn<Questionnaire, String> domainColumn;
	@FXML
	private TableColumn<Questionnaire, String> dateColumn;
	@FXML
    private Button btnRemQuizz;
    @FXML
    private Button btnAddQuizz;
    @FXML
    private Button btnEditQuizz;
    
    @Override
    public void setMainApp(MainApp mainApp) {
    	super.setMainApp(mainApp);
    	ObservableList<Questionnaire> quizzObs = mainApp.getWebGate().getList(Questionnaire.class);
    	tableQuizzList.setItems(quizzObs);
    }
    
	@FXML
	public void initialize(){
		btnRemQuizz.setVisible(false);
		btnEditQuizz.setVisible(true);
		
		nameColumn.setCellValueFactory((CellDataFeatures<Questionnaire, String> feature) -> {
			Questionnaire quizz = feature.getValue();
			return new SimpleObjectProperty<>(quizz.getLibelle());
		});
		domainColumn.setCellValueFactory((CellDataFeatures<Questionnaire, String> feature) -> {
			Questionnaire quizz = feature.getValue();
			return new SimpleObjectProperty<>(quizz.getDomaine().getLibelle());
		});
		dateColumn.setCellValueFactory((CellDataFeatures<Questionnaire, String> feature) -> {
			Questionnaire quizz = feature.getValue();
			return new SimpleObjectProperty<>(quizz.getDate().toString());
		});	
	}

    /**
	 * Edit Quizz
	 * @param event
	 */
    @FXML
    void handleEdit(ActionEvent event) {
    	QuizzController c = (QuizzController) gUtils.switchView("QuizzLayout");
    	c.setQuizz(tableQuizzList.getSelectionModel().getSelectedItem());
    }
	
	/**
	 * Add Quizz
	 * @param event
	 */
    @FXML
    void handleAdd(ActionEvent event) {

    }
    
    /**
	 * Remove Quizz and pass data to quizzLayout
	 * @param event
	 */
    @FXML
    void handleRem(ActionEvent event) {

    }
}
