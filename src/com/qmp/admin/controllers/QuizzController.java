package com.qmp.admin.controllers;

import com.qmp.admin.models.Questionnaire;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class QuizzController extends Controller {
    @FXML
    private Tab tabGeneral;

    @FXML
    private Tab tabQuestions;
   
    
    private Questionnaire quizz;
    
    
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
