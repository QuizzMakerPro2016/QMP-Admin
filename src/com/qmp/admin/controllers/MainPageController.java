package com.qmp.admin.controllers;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Domaine;
import com.qmp.admin.models.Groupe;
import com.qmp.admin.models.Groupe_questionnaire;
import com.qmp.admin.models.Groupe_utilisateur;
import com.qmp.admin.models.Question;
import com.qmp.admin.models.Questionnaire;
import com.qmp.admin.models.Rang;
import com.qmp.admin.models.Utilisateur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class MainPageController extends Controller {
	@FXML
	private Button userButton;

	@FXML
	private Button domainButton;

	@FXML
	private Button quizzButton;

	@FXML
	private Button rankButton;

	@FXML
	private Button dcButton;
	
	@FXML
	private Button groupButton;
	
	@FXML
	private Button homeButton;
	
	@FXML
	private Text userText;
	
	
	public MainPageController() {
	}

	@Override
	public void setMainApp(MainApp mainApp) throws ClientProtocolException, IOException {
		super.setMainApp(mainApp);
		userText.setText(mainApp.getUser().getNom().toUpperCase() + " " + mainApp.getUser().getPrenom());
	}
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		
	}

	public void setActualButton(Button button){
		ArrayList <Button> arrayButton = new ArrayList<Button>();
		arrayButton.add(this.dcButton);
		arrayButton.add(this.domainButton);
		arrayButton.add(this.quizzButton);
		arrayButton.add(this.rankButton);
		arrayButton.add(this.userButton);
		arrayButton.add(this.homeButton);
		arrayButton.add(this.groupButton);
		
		for(Button b : arrayButton){
			if(b.equals(button)){
				button.setStyle("-fx-background-color : #2C98D4");
			}else{
				b.setStyle("-fx-background-color : derive(#37474f, 20%)");
			}
		}
	}
	
	@FXML
	void handleUser(ActionEvent event) throws IOException {
		mainApp.getTaskQueue().getAll(Rang.class);
		mainApp.getTaskQueue().getAllRefresh(Utilisateur.class, 2);
		gUtils.switchView("ManageUserLayout");
		this.setActualButton(userButton);
	}

	@FXML
	void handleRank(ActionEvent event) throws IOException {
		mainApp.getTaskQueue().getAll(Rang.class);
		gUtils.switchView("ManageRankLayout");
		this.setActualButton(rankButton);
	}

	@FXML
	void handleDomain(ActionEvent event) throws IOException {
		mainApp.getTaskQueue().getAll(Domaine.class);
		gUtils.switchView("ManageDomainLayout");
		this.setActualButton(domainButton);
	}

	@FXML
	void handleGroup(ActionEvent event) throws IOException {
		mainApp.getTaskQueue().getAll(Groupe.class, 2);
		mainApp.getTaskQueue().getAll(Questionnaire.class);
		mainApp.getTaskQueue().getAll(Groupe_questionnaire.class);
		mainApp.getTaskQueue().getAll(Utilisateur.class);
		mainApp.getTaskQueue().getAll(Groupe_utilisateur.class);
		gUtils.switchView("ManageGroupLayout");
		this.setActualButton(groupButton);
	}

	@FXML
	void handleHome(ActionEvent event) throws IOException {
		gUtils.switchView("homeLayout");
		this.setActualButton(homeButton);
	}

	@FXML
	void handleQuizz(ActionEvent event) throws IOException {
		mainApp.getTaskQueue().getAll(Questionnaire.class);
		mainApp.getTaskQueue().getAll(Domaine.class);
		mainApp.getTaskQueue().getAll(Question.class);
		gUtils.switchView("QuizzHomeLayout");
		this.setActualButton(quizzButton);

	}

	@FXML
	void handleDisconnect(ActionEvent event) throws IOException {
		mainApp.getRootLayout().setLeft(null);
		mainApp.setUser(null);
		gUtils.switchView("ConnexionLayout");
	}

}
