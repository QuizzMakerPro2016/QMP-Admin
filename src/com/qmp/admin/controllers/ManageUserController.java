package com.qmp.admin.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.client.ClientProtocolException;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Groupe;
import com.qmp.admin.models.Questionnaire;
import com.qmp.admin.models.Rang;
import com.qmp.admin.models.Utilisateur;
import com.qmp.admin.utils.GraphicUtils;
import com.qmp.admin.utils.JBCrypt;
import com.qmp.admin.utils.Notifier;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ManageUserController extends Controller {

	@FXML
	private TableView<Utilisateur> userList;

	@FXML
	private TableColumn<Utilisateur, String> surnameColumn;

	@FXML
	private TableColumn<Utilisateur, String> nameColumn;

	@FXML
	private TableColumn<Utilisateur, String> rankColumn;

	@FXML
	private Button newButton;

	@FXML
	private TextField idField;

	@FXML
	private TextField surnameField;

	@FXML
	private TextField mailField;

	@FXML
	private TextField nameField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private ComboBox<Rang> rankField;

	@FXML
	private Button deleteButton;

	@FXML
	private Button editButton;

	@FXML
	private TableView<Groupe> groupList;

	@FXML
	private TableColumn<Groupe, String> groupListColumn;

	@FXML
	private TableView<Questionnaire> quizzList;

	@FXML
	private TableColumn<Questionnaire, String> quizzListColumn;

	@FXML
	private TextField userSearch;

	private ObservableList<Utilisateur> userObs;

	@Override
	public void setMainApp(MainApp mainApp) throws ClientProtocolException, IOException {
		super.setMainApp(mainApp);

		ObservableList<Rang> r = mainApp.getWebGate().getList(Rang.class);
		userObs = mainApp.getWebGate().getList(Utilisateur.class);
		rankField.setItems(r);
		userList.setItems(userObs);

		// Set fields in an ArrayList to search in fields.
		ArrayList<String> fields = new ArrayList<String>();
		fields.addAll(Arrays.asList("nom", "prenom"));

		// Set filter for the groupList
		setFilterTableView(this.userSearch, this.userList, this.userObs, fields);

	}

	@FXML
	private void initialize() {

		surnameColumn.setCellValueFactory((CellDataFeatures<Utilisateur, String> feature) -> {
			Utilisateur user = feature.getValue();
			return new SimpleObjectProperty<>(user.getNom());
		});

		nameColumn.setCellValueFactory((CellDataFeatures<Utilisateur, String> feature) -> {
			Utilisateur user = feature.getValue();
			return new SimpleObjectProperty<>(user.getPrenom());
		});

		rankColumn.setCellValueFactory((CellDataFeatures<Utilisateur, String> feature) -> {
			Utilisateur user = feature.getValue();
			return new SimpleObjectProperty<>(user.getRang().getLibelle());
		});

		quizzListColumn.setCellValueFactory((CellDataFeatures<Questionnaire, String> feature) -> {
			Questionnaire quizz = feature.getValue();
			return new SimpleObjectProperty<>(quizz.getLibelle());
		});

		groupListColumn.setCellValueFactory((CellDataFeatures<Groupe, String> feature) -> {
			Groupe group = feature.getValue();
			return new SimpleObjectProperty<>(group.getLibelle());
		});

		showUser(null);
		userList.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showUser(newValue));
		
		addFieldsToCheck(nameField, surnameField, mailField);

	}

	public void showUser(Utilisateur user) {

		if (user == null) {
			idField.setText("0");
			surnameField.setText("");
			passwordField.setText("");
			nameField.setText("");
			mailField.setText("");
		} else {
			idField.setText(String.valueOf(user.getId()));
			surnameField.setText(user.getNom());
			nameField.setText(user.getPrenom());
			passwordField.setText("");
			mailField.setText(user.getMail());
			quizzList.setItems(FXCollections.observableArrayList(user.getQuestionnaires()));
			groupList.setItems(FXCollections.observableArrayList(user.getGroupes()));
			rankField.setValue(user.getRang());
		}
	}

	@FXML
	void handleSave(ActionEvent event) throws ClientProtocolException, IOException {
		
		if(!checkFields()) return;
		
		int selInxdex = userList.getSelectionModel().getSelectedIndex();

		if(passwordField.getText().isEmpty() && selInxdex >= 0){
			Notifier.notifyError("Champ vide !", "Veuillez renseigner le champ 'Mot de passe'");
			return;
		}
		if(rankField.getValue() == null){
			Notifier.notifyError("Champ vide !", "Veuillez renseigner le champ 'Rang'");
			return;
		}
		
		
		if (selInxdex >= 0) {
			// Update
			Utilisateur selectedUser = userList.getSelectionModel().getSelectedItem();
			selectedUser.setNom(surnameField.getText());
			selectedUser.setPrenom(nameField.getText());
			selectedUser.setMail(mailField.getText());
			selectedUser.setIdRang(rankField.getValue().getId());

			if (passwordField.getText() != "") {
				selectedUser.setPassword(JBCrypt.hashpw(passwordField.getText(), JBCrypt.gensalt()));
			}

			try {
				String res = mainApp.getWebGate().update(selectedUser, selectedUser.getId());
				checkResult(Utilisateur.class, res, "Utilisateur '{{object}}' mis à jour.");
				mainApp.getTaskQueue().getAll(Utilisateur.class);
			} catch (Exception e) {
				GraphicUtils.showException(e);
			}
		} else {
			// Insertion
			Utilisateur user = new Utilisateur();
			user.setNom(surnameField.getText());
			user.setPrenom(nameField.getText());
			user.setMail(mailField.getText());
			user.setPassword(JBCrypt.hashpw(passwordField.getText(), JBCrypt.gensalt()));
			user.setIdRang(rankField.getValue().getId());
			user.setRang(rankField.getValue());

			try {
				String res = mainApp.getWebGate().add(user);
				Utilisateur u = (Utilisateur) checkResult(Utilisateur.class, res, "Utilisateur '{{object}}' ajouté.");
				if(u != null){
					u.setIdRang(rankField.getValue().getId());
					u.setRang(rankField.getValue());
					mainApp.getWebGate().getList(Utilisateur.class).add(u);
					showUser(u);
				}
			} catch (Exception e) {
				GraphicUtils.showException(e);
			}

		}
	}

	@FXML
	void handleNew(ActionEvent event) {
		showUser(null);
		userList.getSelectionModel().clearSelection();
	}

	@FXML
	void handleDelete(ActionEvent event) {

		int selInxdex = userList.getSelectionModel().getSelectedIndex();
		Utilisateur selectedUser = userList.getSelectionModel().getSelectedItem();
		if (selInxdex >= 0) {
			boolean response = gUtils.showDialog("Suppression", "Supprimer un utilisateur ?",
					"Voulez-vous vraiment supprimer l'utilisateur '" + selectedUser.getMail() + "' ?");
			if (response) {
				userObs.remove(selInxdex);
				
				try {
					String res = mainApp.getWebGate().delete(selectedUser, selectedUser.getId());
					checkResult(Utilisateur.class, res, "Utilisateur '{{object}}' supprimé.");
				} catch (Exception e) {
					GraphicUtils.showException(e);
				}
			}
		} else {
			Notifier.notifyWarning("Attention", "Aucun Utilisateur sélectionné");
		}
	}

}
