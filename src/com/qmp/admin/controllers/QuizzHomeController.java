package com.qmp.admin.controllers;

import com.qmp.admin.models.Domaine;
import com.qmp.admin.models.Questionnaire;
import com.qmp.admin.models.Utilisateur;
import com.sun.media.jfxmedia.logging.Logger;

import javafx.beans.property.SimpleObjectProperty;
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
    
	@FXML
	public void initialize(){
		btnRemQuizz.setVisible(false);
		btnEditQuizz.setVisible(false);
		
		nameColumn.setCellValueFactory((CellDataFeatures<Questionnaire, String> feature) -> {
			Questionnaire quizz = feature.getValue();
			return new SimpleObjectProperty<>(quizz.getLibelle());
		});
		domainColumn.setCellValueFactory((CellDataFeatures<Questionnaire, String> feature) -> {
			Questionnaire quizz = feature.getValue();
			return new SimpleObjectProperty<>(quizz.getDomaine().getLibelle());
		});
		/*dateColumn.setCellValueFactory((CellDataFeatures<Questionnaire, String> feature) -> {
			Questionnaire quizz = feature.getValue();
			return new SimpleObjectProperty<>(quizz.getDate());
		});*/
	}
	
	/**
	 * Add Quizz
	 * @param event
	 */
    @FXML
    void handleAdd(ActionEvent event) {

    }
    
    /**
	 * Edit Quizz
	 * @param event
	 */
    @FXML
    void handleEdit(ActionEvent event) {

    }
    
    /**
	 * Remove Quizz and pass data to quizzLayout
	 * @param event
	 */
    @FXML
    void handleRem(ActionEvent event) {

    }
}
