package com.qmp.admin.controllers;

import java.io.IOException;
import java.util.List;

import com.qmp.admin.models.Question;
import com.qmp.admin.models.Questionnaire;
import com.qmp.admin.models.Reponse;
import com.qmp.admin.utils.GraphicUtils;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class QuizzController extends Controller {

	@FXML
	private Tab tabGeneral;

	@FXML
	private Tab tabQuestions;

	@FXML
	private TableView<Question> tableQuestionsList;

	@FXML
	private TableColumn<Question, String> tableQuestionsCol;

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

	@FXML
	private void initialize() {
		this.quizz = null;

		tableQuestionsCol.setCellValueFactory((CellDataFeatures<Question, String> feature) -> {
			Question quest = feature.getValue();
			return new SimpleObjectProperty<>(quest.getLibelle());
		});

		tableAnsListCol.setCellValueFactory((CellDataFeatures<Reponse, String> feature) -> {
			Reponse ans = feature.getValue();
			return new SimpleObjectProperty<>(ans.getLibelle());
		});

		tableQuestionsList.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showQuestion(newValue));

		cbMultiQuest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				switchQuestionType(false);
			}
		});

		cbOpenQuest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				switchQuestionType(true);
			}
		});

	}

	/**
	 * Add answer to question
	 * 
	 * @param event
	 */
	@FXML
	void handleAddAns(ActionEvent event) {

	}

	/**
	 * Add question to quizz
	 * 
	 * @param event
	 */
	@FXML
	void handleAddQuest(ActionEvent event) {

	}

	/**
	 * Click on "Question Choix Multiple" option (hide tfUniqueAns + show
	 * answers table)
	 * 
	 * @param event
	 */
	@FXML
	void handleQuestCM(ActionEvent event) {

	}

	/**
	 * Click on "Question Ouverte" option (show tfUniqueAns + hide answers
	 * table)
	 * 
	 * @param event
	 */
	@FXML
	void handleQuestO(ActionEvent event) {

	}

	/**
	 * Remove Answer from quizz and from DB
	 * 
	 * @param event
	 */
	@FXML
	void handleRemAns(ActionEvent event) {

	}

	/**
	 * Remove Question from quizz IF not used and creator delete from DB ?
	 * 
	 * @param event
	 */
	@FXML
	void handleRemQuest(ActionEvent event) {

	}

	public Questionnaire getQuizz() {
		return quizz;
	}

	public void setQuizz(Questionnaire quizz) {
		this.quizz = quizz;
		showQuizzGeneral();
		showQuizzQuestions();
	}

	private void showQuizzGeneral() {
		// TODO - Nicolas
	}

	private void showQuizzQuestions() {
		tableQuestionsList.setItems(FXCollections.observableArrayList(this.quizz.getQuestions()));
		showQuestion(null);
	}

	private void showQuestion(Question q) {
		if (q != null) {

			if (q.getReponses().isEmpty()) {
				List<Reponse> rep = null;
				try {
					rep = (List<Reponse>) mainApp.getWebGate().getMembers(Question.class, q.getId(), "reponses",
							Reponse.class);
					q.setReponses(rep);
				} catch (IOException e) {
					GraphicUtils.showException(e);
				}
			}

			tfQuestLibelle.setText(q.getLibelle());

			showAnswers(q);
		} else {
			cbMultiQuest.setSelected(false);
			cbOpenQuest.setSelected(false);
			tfQuestLibelle.setText("");
			tableAnsList.setVisible(false);
			tfUniqueAns.setVisible(false);
		}
	}

	private void showAnswers(Question q) {
		if (q.isType()) {
			// Open
			tfUniqueAns.setVisible(true);
			tableAnsList.setVisible(false);
			if (q.getReponses().size() < 1) {
				tfUniqueAns.setId("0");
				return;
			}
			tfUniqueAns.setText(q.getReponses().get(0).getLibelle());
			tfUniqueAns.setId(String.valueOf(q.getReponses().get(0).getId()));
			cbMultiQuest.setSelected(false);
			cbOpenQuest.setSelected(true);
		} else {
			// Multiple
			tfUniqueAns.setVisible(false);
			tableAnsList.setVisible(true);
			tableAnsList.setItems(FXCollections.observableArrayList(q.getReponses()));
			cbMultiQuest.setSelected(true);
			cbOpenQuest.setSelected(false);
		}

	}

	private void switchQuestionType(Boolean isOpenNew) {
		cbOpenQuest.setSelected(isOpenNew);
		cbMultiQuest.setSelected(!isOpenNew);
		Question q = tableQuestionsList.getSelectionModel().getSelectedItem();
		q.setType(isOpenNew);
		showAnswers(q);
	}

}
