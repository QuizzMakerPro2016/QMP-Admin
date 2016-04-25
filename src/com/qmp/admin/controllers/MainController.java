package com.qmp.admin.controllers;

import java.io.IOException;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Utilisateur;
import com.qmp.admin.utils.GraphicUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class MainController extends Controller {

	@FXML
	private TextField loginField;
	@FXML
	private TextField passwordField;
	@FXML
	private Button connectButton;

	@FXML
	private Text errorText;

	private Utilisateur user;

	public MainController() {

	}

	@Override
	public void setMainApp(MainApp mainApp) {
		super.setMainApp(mainApp);
		gUtils = new GraphicUtils(mainApp);
		connectButton.setDefaultButton(true);

	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

	}

	@FXML
	private void handleConnect(ActionEvent event) throws IOException {
		Utilisateur result = mainApp.getWebGate().connect(loginField.getText(), passwordField.getText());
		mainApp.setUser(result);
		if (mainApp.isLogged() == true) {
			if (mainApp.isAdmin()) {
				errorText.setText(
						"Connection réussie de " + mainApp.getUser().getPrenom() + " " + mainApp.getUser().getNom());
				GraphicUtils.notifySuccess("Connexion réussie", "Connecté en tant que : "+ mainApp.getUser().getPrenom() + " " + mainApp.getUser().getNom());
				gUtils.loadMenu();
				gUtils.switchView("HomeLayout");

			} else {
				errorText.setText("Cet utilisateur ne dispose pas des droits nécessaires ("
						+ result.getRang().getLibelle() + ")");
			}
		} else {
			errorText.setText("Connexion échouée");
			GraphicUtils.notifyError("Connexion échouée", "Identifaint ou mot de passe incorrect");
		}
		
	}
}
