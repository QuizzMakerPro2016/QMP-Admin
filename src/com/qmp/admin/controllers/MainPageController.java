package com.qmp.admin.controllers;

import java.io.IOException;

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
	private Text userText;

	public MainPageController() {
	}

	@Override
	public void setMainApp(MainApp mainApp) {
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

	@FXML
	void handleUser(ActionEvent event) throws IOException {
		mainApp.getTaskQueue().getAll(Rang.class);
		mainApp.getTaskQueue().getAll(Utilisateur.class, 2);
		gUtils.switchView("ManageUserLayout");
	}

	@FXML
	void handleRank(ActionEvent event) throws IOException {
		mainApp.getTaskQueue().getAll(Rang.class);
		gUtils.switchView("ManageRankLayout");
	}

	@FXML
	void handleDomain(ActionEvent event) throws IOException {
		mainApp.getTaskQueue().getAll(Domaine.class);
		gUtils.switchView("ManageDomainLayout");
	}

	@FXML
	void handleGroup(ActionEvent event) throws IOException {
		mainApp.getTaskQueue().getAll(Groupe.class, 2);
		mainApp.getTaskQueue().getAll(Questionnaire.class);
		mainApp.getTaskQueue().getAll(Groupe_questionnaire.class);
		mainApp.getTaskQueue().getAll(Utilisateur.class);
		mainApp.getTaskQueue().getAll(Groupe_utilisateur.class);
		gUtils.switchView("ManageGroupLayout");
	}

	@FXML
	void handleHome(ActionEvent event) throws IOException {
		gUtils.switchView("homeLayout");
	}

	@FXML
	void handleQuizz(ActionEvent event) throws IOException {
		mainApp.getTaskQueue().getAll(Question.class);
		gUtils.switchView("QuizzHomeLayout");

	}

	@FXML
	void handleDisconnect(ActionEvent event) throws IOException {
		mainApp.getRootLayout().setLeft(null);
		mainApp.setUser(null);
		gUtils.switchView("ConnexionLayout");
	}

}
