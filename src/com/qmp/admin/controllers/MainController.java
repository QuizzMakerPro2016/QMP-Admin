package com.qmp.admin.controllers;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Utilisateur;
import com.qmp.admin.utils.GraphicUtils;
import com.qmp.admin.utils.Notifier;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class MainController extends Controller {

	@FXML
	private TextField loginField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Button connectButton;

	@FXML
	private Text errorText;

	private Utilisateur user;

	public MainController() {

	}

	@Override
	public void setMainApp(MainApp mainApp) throws ClientProtocolException, IOException {
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
				Notifier.notifySuccess("Connexion réussie", "Connecté en tant que : "+ mainApp.getUser().getPrenom() + " " + mainApp.getUser().getNom());
				gUtils.loadMenu();
				gUtils.switchView("HomeLayout");

			} else {
				Notifier.notifyError("Connexion échouée", "Cet utilisateur ne dispose pas des droits nécessaires");
			}
		} else {
			errorText.setText("Connexion échouée");
			Notifier.notifyError("Connexion échouée", "Identifaint ou mot de passe incorrect");
		}
		
	}
}
