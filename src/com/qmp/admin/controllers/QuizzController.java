package com.qmp.admin.controllers;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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

    @FXML
    private MenuButton btnAnsAction;

    @FXML
    private CheckBox checkIsTrue;
    
    @FXML
    private TextField tfAnsLibelle;
    
    @FXML
    private Label lblUniqueAns;
    
    private boolean isQuestionModified;
    private Questionnaire quizz;
    
    

	@Override
	public void setMainApp(MainApp mainApp) throws ClientProtocolException, IOException {
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
		
		tableAnsList.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> showAnwser(newValue));

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
		
		tfAnsLibelle.setVisible(false);
		checkIsTrue.setVisible(false);
		btnAnsAction.setVisible(false);
		lblUniqueAns.setVisible(false);
		
		
		addFieldsToCheck(libelleQuizz);

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
    	showAnwser(null);
    	tableAnsList.getSelectionModel().clearSelection();
    }

    @FXML
    void handleSaveAns(Event event){
    	
    	Question q = saveQuestion();
    	if(q == null)
    		return;
    	
    	Reponse reponse = tableAnsList.getSelectionModel().getSelectedItem();
    	if(reponse == null){
    		reponse = new Reponse();
    	}
    	
    	if(Integer.valueOf(tfAnsLibelle.getId()) > 0){
    		reponse.setId(Integer.valueOf(tfAnsLibelle.getId()));
    	}
    	
    	reponse.setLibelle(tfAnsLibelle.getText().toString());
    	reponse.setGood(checkIsTrue.isSelected());
    	reponse.setIdQuestion(q.getId());
    	
    	if(reponse.getId()>0){
    		reponse = (Reponse) updateObject(reponse, reponse.getId());
    	}else{
    		reponse = (Reponse) addObject(reponse);
    	}
    	
    	try {
			q.setReponses(mainApp.getWebGate().getMembers(Question.class, q.getId(), "reponses", Reponse.class));
	    	showQuestion(q);
		} catch (IOException e) {
			GraphicUtils.showException(e);
		}
    	
    }
    
    /**
     * Remove Answer from quizz and from DB
     * @param event
     */
    @FXML
    void handleRemAns(ActionEvent event) {
    	Reponse reponse = tableAnsList.getSelectionModel().getSelectedItem();
    	if(reponse != null){
    		boolean ok = gUtils.showDialog("Suppression", "Supprimer une réponse?", "Voulez-vous vraiment supprimer la réponse '"+reponse.getLibelle()+"'");
    		if(ok){
    			deleteObject(reponse, reponse.getId());
    			tableAnsList.getItems().remove(reponse);
    		}
    	}else{
    		Notifier.notifyWarning("Attention", "Aucune réponse sélectionnée.");
    	}
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
			mainApp.getWebGate().deleteRelation(Question_questionnaire.class, q.getId(), this.quizz.getId());
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
    	saveQuestion();
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
		showAnwser(null);
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
			tfQuestLibelle.setId(String.valueOf(q.getId()));

			showAnswers(q);
		} else {
			cbMultiQuest.setSelected(false);
			cbOpenQuest.setSelected(false);
			tfQuestLibelle.setText("");
			tfQuestLibelle.setId("0");
			tableAnsList.setVisible(false);
			tfUniqueAns.setVisible(false);
		}
	}

	private void showAnwser(Reponse r){
		if(r != null){
			tfAnsLibelle.setText(r.getLibelle());
			checkIsTrue.setSelected(r.isGood());
			tfAnsLibelle.setId(String.valueOf(r.getId()));
		}else{
			tfAnsLibelle.setText("");
			checkIsTrue.setSelected(false);
			tfAnsLibelle.setId("0");
		}
	}
	
	private void showAnswers(Question q) {
		tfUniqueAns.setVisible(q.isType());
		tableAnsList.setVisible(!q.isType());
		cbMultiQuest.setSelected(!q.isType());
		cbOpenQuest.setSelected(q.isType());
		tfAnsLibelle.setVisible(!q.isType());
		checkIsTrue.setVisible(!q.isType());
		btnAnsAction.setVisible(!q.isType());
		lblUniqueAns.setVisible(q.isType());


		if (q.isType()) {
			// Open
			if (q.getReponses().size() < 1) {
				tfUniqueAns.setId("0");
				return;
			}
			tfUniqueAns.setText(q.getReponses().get(0).getLibelle());
			tfUniqueAns.setId(String.valueOf(q.getReponses().get(0).getId()));
		} else {
			// Multiple
			tableAnsList.setItems(FXCollections.observableArrayList(q.getReponses()));
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
	 
	 private Questionnaire saveQuizz(){
		 
		 if(!checkFields()) return null;
		 
		 if(this.quizz==null) 
			 this.quizz=new Questionnaire();
	    	
    	this.quizz.setDescription(descQuizz.getText());
    	this.quizz.setLibelle(libelleQuizz.getText());
    	this.quizz.setIdDomaine(cbDomain.getSelectionModel().getSelectedItem().getId());
    	this.quizz.setIdUtilisateur(mainApp.getUser().getId());
    	
    	Instant instant = dateQuizz.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
    	java.sql.Date date = new java.sql.Date(Date.from(instant).getTime());
		this.quizz.setDate(date);
    			
		if (this.quizz.getId() > 0) {
			//Update
			this.quizz = (Questionnaire) updateObject(this.quizz, this.quizz.getId());
		} else {
			//Add
			this.quizz = (Questionnaire) addObject(this.quizz);
		}
		return this.quizz;
	 }
	 
	 private Question saveQuestion(){
 		if(saveQuizz() == null) return null;
 		
 		if(!checkFields()) return null;
    	
 		Question q = tableQuestionsList.getSelectionModel().getSelectedItem();
    	if(q == null)
    		q = new Question();
    	
    	if(Integer.valueOf(tfQuestLibelle.getId()) > 0)
    		q.setId(Integer.valueOf(tfQuestLibelle.getId()));
    	
    	q.setIdUtilisateur(mainApp.getUser().getId());
		q.setLibelle(tfQuestLibelle.getText());
		q.setType(cbOpenQuest.isSelected());
		
		if (q.getId() > 0) {
			//Update
			q = (Question) updateObject(q, q.getId());
			if(q != null)
    			showQuizzQuestions(q);
				
		} else {
			//Add
			q = (Question) addObject(q);
			if(q != null)
				quizz.getQuestions().add(q);
		}
	    	
		if(q == null)
			return null;
	    	
	    affectQuestToQuizz(q);
			
			//IF Open Question, auto save answer
		if(q.isType()){
			Reponse rep = new Reponse();
			rep.setIdQuestion(q.getId());
			rep.setGood(true);
			// Makes some weird things....
			rep.setLibelle(tfUniqueAns.getText());
			
			rep.setId(Integer.valueOf(tfUniqueAns.getId()));
			
			if(rep.getId() > 0 ){
				//Update
				rep = (Reponse) updateObject(rep, rep.getId());
				
			} else {
				//Add
				rep = (Reponse) addObject(rep);
				if(rep != null){
					q.getReponses().add(rep);
				}
			}
			
		}
		showQuizzQuestions(q);
		return q;
	 }
}
