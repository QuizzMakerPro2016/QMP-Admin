package com.qmp.admin.controllers;

import com.qmp.admin.models.Questionnaire;
import com.qmp.admin.models.Reponse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class QuizzController extends Controller {
    @FXML
    private Tab tabGeneral;

    @FXML
    private Tab tabQuestions;  

    @FXML
    private TableView<?> tableQuestionsList;

    @FXML
    private Button btnRemQuest;

    @FXML
    private Button btnAddQuest;

    @FXML
    private TextField tfQuestLibelle;

    @FXML
    private RadioButton cbOpenQuest;

    @FXML
    private RadioButton cbMultiQuest;

    @FXML
    private TextField tfUniqueAns;

    @FXML
    private TableView<Reponse> tableAnsList;

    @FXML
    private TableColumn<Reponse, String> tableAnsListCol;

    @FXML
    private Button btnRemAns;

    @FXML
    private Button btnAddAns;
    
    private Questionnaire quizz;

	/**
	 * Add answer to question
	 * @param event
	 */
    @FXML
    void handleAddAns(ActionEvent event) {

    }
    
    /**
     * Add question to quizz
     * @param event
     */
    @FXML
    void handleAddQuest(ActionEvent event) {

    }

    /**
     * Click on "Question Choix Multiple" option
     * (hide tfUniqueAns + show answers table)
     * @param event
     */
    @FXML
    void handleQuestCM(ActionEvent event) {

    }

    /**
     * Click on "Question Ouverte" option
     * (show tfUniqueAns + hide answers table)
     * @param event
     */
    @FXML
    void handleQuestO(ActionEvent event) {

    }

    /**
     * Remove Answer from quizz and from DB
     * @param event
     */
    @FXML
    void handleRemAns(ActionEvent event) {

    }

    /**
     * Remove Question from quizz
     * IF not used and creator delete from DB ?
     * @param event
     */
    @FXML
    void handleRemQuest(ActionEvent event) {

    }

	@FXML
	private void initialize() {
		this.quizz = null;
	}

	public Questionnaire getQuizz() {
		return quizz;
	}

	public void setQuizz(Questionnaire quizz) {
		this.quizz = quizz;
		showQuizzGeneral();
		showQuizzQuestions();
	}
	
	private void showQuizzGeneral(){
		//TODO
	}
	
	private void showQuizzQuestions(){
		//TODO
	}

}
