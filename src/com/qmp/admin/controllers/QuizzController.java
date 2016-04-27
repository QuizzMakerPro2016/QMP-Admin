package com.qmp.admin.controllers;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Domaine;
import com.qmp.admin.models.Question;
import com.qmp.admin.models.Question_questionnaire;
import com.qmp.admin.models.Questionnaire;
import com.qmp.admin.models.Reponse;
import com.qmp.admin.utils.GraphicUtils;
import com.qmp.admin.utils.Logger;
import com.qmp.admin.utils.Notifier;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

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
    @FXML
    private Button btnSaveQuest;
    @FXML
    private MenuButton btnSearchQuest;
    @FXML
    private TextField libelleQuizz;
    @FXML
    private DatePicker dateQuizz;
    @FXML
    private ComboBox<Domaine> cbDomain;
    @FXML
    private TextArea descQuizz;
    
    @FXML
    private Button btnSaveQuizz;   
    
    private boolean isQuestionModified;
    private Questionnaire quizz;
    
    

	@Override
	public void setMainApp(MainApp mainApp) {
		super.setMainApp(mainApp);
		cbDomain.setItems(FXCollections.observableArrayList(mainApp.getWebGate().getList(Domaine.class)));
	}

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
		
		tfQuestLibelle.setOnKeyTyped(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				isQuestionModified = true;				
			}
		});

	}
	
	 /**
     * Save Quizz
     * @param event
     */
    @FXML
    void handleSaveQuizz(ActionEvent event) {
    	saveQuizz();
    }

	/**
	 * Add answer to question
	 * 
	 * @param event
	 */
    @FXML
    void handleAddAns(ActionEvent event) {
    	//TODO Nicolas
    }
    
    /**
     * Add question to quizz
     * @param event
     */
    @FXML
    void handleAddQuest(ActionEvent event) {
    	showQuestion(null);
    	tableQuestionsList.getSelectionModel().clearSelection();
    }


    /**
     * Remove Answer from quizz and from DB
     * @param event
     */
    @FXML
    void handleRemAns(ActionEvent event) {
    	//TODO Nicolas
    }

    /**
     * Remove Question from quizz
     * IF not used and creator delete from DB ?
     * @param event
     */
    @FXML
    void handleRemQuest(ActionEvent event) {
    	Question q = tableQuestionsList.getSelectionModel().getSelectedItem();
    	if(q == null){
    		Notifier.notifyError("Erreur", "Aucune question sélectionnée");
    		return; 
    	}
    	
    	try {
			mainApp.getWebGate().deleteRelation(Question_questionnaire.class, this.quizz.getId(), q.getId());
			this.quizz.getQuestions().remove(q);
			showQuizzQuestions(null);
		} catch (Exception e) {
			GraphicUtils.showException(e);
		}
    }
    
    /**
     * Saves Current Question
     * @param event
     */
    @FXML
    void handleSaveQuest(ActionEvent event) {
    	Question q = tableQuestionsList.getSelectionModel().getSelectedItem();
    	
    	//Empty Fields Check
    	if(tfQuestLibelle.getText().isEmpty()){
    		Notifier.notifyError("Impossible d'enregitrer la question", "Veuillez renseigner le libellé de la question");
    		return;
    	}
    	if(!cbOpenQuest.isSelected() && !cbMultiQuest.isSelected()){
    		Notifier.notifyError("Impossible d'enregitrer la question", "Veuillez renseigner le type de la question");
    		return;
    	}  
    	if(cbOpenQuest.isSelected() && tfUniqueAns.getText().isEmpty()){
    		Notifier.notifyError("Impossible d'enregitrer la question", "Veuillez renseigner la réponse à la question");
    		return;
    	}
    	
    	if(q != null){
    		//Update
    		q.setLibelle(tfQuestLibelle.getText());
    		
    		try {
				mainApp.getWebGate().update(q, q.getId());
    			showQuizzQuestions(q);

			} catch (Exception e) {
				GraphicUtils.showException(e);
			}
    		
    	}else{
    		//Insert
    		Question newQuest = new Question();
    		newQuest.setIdUtilisateur(mainApp.getUser().getId());
    		newQuest.setLibelle(tfQuestLibelle.getText());
    		newQuest.setType(cbOpenQuest.isSelected());
    		quizz.getQuestions().add(newQuest);    		

    		
    		saveQuizz();
    		
    		try {
				String resQuest = mainApp.getWebGate().add(newQuest);
				q = (Question) mainApp.getWebGate().getObjectFromJson(resQuest, Question.class);				
			} catch (IllegalArgumentException | IllegalAccessException | IOException e) {
				GraphicUtils.showException(e);
			}
    		
    		
    	}
    	
    	if(q.getId() < 1){
    		Notifier.notifyError("Impossible de lier la question au quizz", "Erreur lors de l'enregistrement de la question");
    		return;
    	}
    	
    	affectQuestToQuizz(q);
		
		//IF Open Question, auto save answer
		if(q.isType()){

			Reponse rep = new Reponse();
			rep.setIdQuestion(q.getId());
			rep.setGood(true);
			// Makes some weird things....
			rep.setLibelle(tfUniqueAns.getText());
			
			try {
				int id  = Integer.valueOf(tfUniqueAns.getId());
				if( id > 0){
					rep.setId(id);
					mainApp.getWebGate().update(rep, rep.getId());
				}else{
					String resRep = mainApp.getWebGate().add(rep);
					Reponse repRes = (Reponse) mainApp.getWebGate().getObjectFromJson(resRep, Reponse.class);
					q.getReponses().add(repRes);
				}
			} catch (Exception e) {
				GraphicUtils.showException(e);
			}
		}
		showQuizzQuestions(q);
    }
    
    @FXML
    void handleSearchQuest(Event event) {
		if(!isQuestionModified)
			return;
		final MenuItem wizPopup = new MenuItem();
	    wizPopup.setGraphic(createPopupContent());
	    btnSearchQuest.getItems().clear();
	    btnSearchQuest.getItems().add(wizPopup);
	    isQuestionModified = false;
	    Logger.log(String.valueOf(isQuestionModified));
    }

	public Questionnaire getQuizz() {
		return quizz;
	}

	public void setQuizz(Questionnaire quizz) {
		this.quizz = quizz;
		showQuizzGeneral();
		showQuizzQuestions(null);
	}

	private void showQuizzGeneral() {
		libelleQuizz.setText(quizz.getLibelle());
		
		Date date = quizz.getDate();
		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDate resDateQuizz = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
		dateQuizz.setValue(resDateQuizz);
		
		cbDomain.getSelectionModel().select(quizz.getDomaine());
		descQuizz.setText(quizz.getDescription());
	}
	
	private void showQuizzQuestions(Question selectedQuestion){
		tableQuestionsList.getItems().clear();
		tableQuestionsList.setItems(FXCollections.observableArrayList(this.quizz.getQuestions()));
		tableQuestionsList.getSelectionModel().select(selectedQuestion);
		showQuestion(selectedQuestion);
	}
		
	private void showQuestion(Question q){
		isQuestionModified = true;
		if(q != null){
			
			if(q.getReponses().isEmpty() && q.getId() > 0){
				List<Reponse> rep = null;
				try {
					rep = (List<Reponse>) mainApp.getWebGate().getMembers(Question.class, q.getId(), "reponses", Reponse.class);
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
			cbMultiQuest.setSelected(false);
			cbOpenQuest.setSelected(true);
			if (q.getReponses().size() < 1) {
				tfUniqueAns.setId("0");
				return;
			}
			tfUniqueAns.setText(q.getReponses().get(0).getLibelle());
			tfUniqueAns.setId(String.valueOf(q.getReponses().get(0).getId()));
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
		if(q == null){
			q = new Question();
		}
		q.setType(isOpenNew);
		showAnswers(q);

	}
	
	private void affectQuestToQuizz(Question q){
		//Affecting Question to quizz (if necessary)
		Question_questionnaire link = new Question_questionnaire();
		link.setIdQuestion(q.getId());
		link.setIdQuestionnaire(quizz.getId());
		
		try {
			boolean isQuestionLinked = mainApp.getWebGate().doRelationExists(Question_questionnaire.class, link.getIdQuestion(), link.getIdQuestionnaire());
			if(!isQuestionLinked)
				mainApp.getWebGate().add(link);
		} catch (Exception e) {
			GraphicUtils.showException(e);
		}
	}
	
	 private VBox createPopupContent() {
		final TableView<Question> table = new TableView<>();
		
		List<Question> questions = new ArrayList<>();
		String text = tfQuestLibelle.getText().toLowerCase();
		for (Question q : mainApp.getWebGate().getList(Question.class)) {
			if(q.getLibelle().toLowerCase().contains(text))
				questions.add(q);
		}
		TableColumn<Question, String> tableCol = new TableColumn<>("Questions");
		tableCol.setCellValueFactory((CellDataFeatures<Question, String> feature) -> {
			Question quest = feature.getValue();
			return new SimpleObjectProperty<>(quest.getLibelle());
		});
		table.getColumns().add(tableCol);
		table.setItems(FXCollections.observableArrayList(questions));

		final Button add = new Button("Ajouter");
		final VBox popup = new VBox(5);
		popup.setAlignment(Pos.CENTER);
		popup.getChildren().setAll(table, add);
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent t) {
				Question q = table.getSelectionModel().getSelectedItem();
				if(q == null)
					return;
				affectQuestToQuizz(q);
				quizz.getQuestions().add(q); 
				showQuizzQuestions(q);
			}
	});

		return popup;
	 }
	 
	 private void saveQuizz(){
		 if(this.quizz==null){
	    		this.quizz=new Questionnaire();
	    	}
	    	this.quizz.setDescription(descQuizz.getText());
	    	this.quizz.setLibelle(libelleQuizz.getText());
	    	this.quizz.setIdDomaine(cbDomain.getSelectionModel().getSelectedItem().getId());
	    	this.quizz.setIdUtilisateur(mainApp.getUser().getId());
	    	
	    	Instant instant = dateQuizz.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
	    	java.sql.Date date = new java.sql.Date(Date.from(instant).getTime());
			this.quizz.setDate(date);
			
			try {
				String res = null;
				if(this.quizz.getId() > 0){
					res = mainApp.getWebGate().update(quizz, quizz.getId());
					checkResult(Questionnaire.class, res, "Questionnaire '{{object}}' mis à jour.");		
				}else{
					res = mainApp.getWebGate().add(quizz);
					checkResult(Questionnaire.class, res, "Questionnaire '{{object}}' ajouté.");
				}
			} catch (IllegalArgumentException | IllegalAccessException | IOException e) {
					GraphicUtils.showException(e);
			}
	 }
}
