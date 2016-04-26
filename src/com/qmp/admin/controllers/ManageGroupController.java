package com.qmp.admin.controllers;

import java.util.ArrayList;
import java.util.Arrays;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Groupe;
import com.qmp.admin.models.Groupe_questionnaire;
import com.qmp.admin.models.Groupe_utilisateur;
import com.qmp.admin.models.Questionnaire;
import com.qmp.admin.models.Utilisateur;
import com.qmp.admin.utils.GraphicUtils;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ManageGroupController extends Controller {

	@FXML
	private TableView<Groupe> groupList;

	@FXML
	private TableColumn<Groupe, String> codeColumn;

	@FXML
	private TableColumn<Groupe, String> libelleColumn;

	@FXML
	private TextField idField;

	@FXML
	private TextField codeField;

	@FXML
	private TextField libelleField;

	@FXML
	private TableView<Utilisateur> userList;

	@FXML
	private TableColumn<Utilisateur, String> userColumn;

	@FXML
	private TableView<Questionnaire> quizzList;

	@FXML
	private TableColumn<Questionnaire, String> quizzColumn;

	@FXML
	private Button editUserButton;

	@FXML
	private Button editQuizzButton;

	@FXML
	private TableView<Questionnaire> quizzIncludedList;

	@FXML
	private TableColumn<Questionnaire, String> quizzIncludedColumn;

	@FXML
	private TableView<Questionnaire> quizzActualIncludedList;

	@FXML
	private TableColumn<Questionnaire, String> quizzActualIncludedColumn;

	@FXML
	private TableView<Utilisateur> userIncludedList;

	@FXML
	private TableColumn<Utilisateur, String> userIncludedColumn;

	@FXML
	private TableView<Utilisateur> userActualIncludedList;

	@FXML
	private TableColumn<Utilisateur, String> userActualIncludedColumn;

	@FXML
	private TabPane tabPane;

	@FXML
	private TextField groupSearch;

	private ObservableList<Groupe> groupObs;
	private ObservableList<Questionnaire> allQuizz;
	private ObservableList<Utilisateur> allUsers;

	@Override
	public void setMainApp(MainApp mainApp) {
		super.setMainApp(mainApp);
		this.groupObs = mainApp.getWebGate().getList(Groupe.class);
		this.allQuizz = mainApp.getWebGate().getList(Questionnaire.class);
		this.allUsers = mainApp.getWebGate().getList(Utilisateur.class);
		groupList.setItems(groupObs);

		// Set fields in an ArrayList to search in fields.
		ArrayList<String> fields = new ArrayList<String>();
		fields.addAll(Arrays.asList("libelle", "code"));

		// Set filter for the groupList
		setFilterTableView(this.groupSearch, this.groupList, this.groupObs, fields);

	}

	@FXML
	private void initialize() {

		libelleColumn.setCellValueFactory((CellDataFeatures<Groupe, String> feature) -> {
			Groupe groupe = feature.getValue();
			return new SimpleObjectProperty<>(groupe.getLibelle());
		});

		codeColumn.setCellValueFactory((CellDataFeatures<Groupe, String> feature) -> {
			Groupe groupe = feature.getValue();
			return new SimpleObjectProperty<>(groupe.getCode());
		});

		userColumn.setCellValueFactory((CellDataFeatures<Utilisateur, String> feature) -> {
			Utilisateur user = feature.getValue();
			return new SimpleObjectProperty<>(user.getNom() + " " + user.getPrenom());
		});

		quizzColumn.setCellValueFactory((CellDataFeatures<Questionnaire, String> feature) -> {
			Questionnaire quizz = feature.getValue();
			return new SimpleObjectProperty<>(quizz.getLibelle());
		});

		showGroup(null);
		groupList.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showGroup(newValue));

	}

	public void showGroup(Groupe group) {
		if (group == null) {
			idField.setText("0");
			libelleField.setText("");
			codeField.setText("");
		} else {
			idField.setText(String.valueOf(group.getId()));
			codeField.setText(group.getCode());
			libelleField.setText(group.getLibelle());
			userList.setItems(FXCollections.observableArrayList(group.getUtilisateurs()));
			quizzList.setItems(FXCollections.observableArrayList(group.getQuestionnaires()));
		}
	}

	@FXML
	void handleSave(ActionEvent event) {
		int selInxdex = groupList.getSelectionModel().getSelectedIndex();
		if (selInxdex >= 0) {
			// Update
			Groupe selectedGroup = groupList.getSelectionModel().getSelectedItem();
			selectedGroup.setLibelle(libelleField.getText());
			selectedGroup.setCode(codeField.getText());
			try {
				mainApp.getWebGate().update(selectedGroup, selectedGroup.getId());
				mainApp.getTaskQueue().getAll(Groupe.class);
			} catch (Exception e) {
				GraphicUtils.showException(e);
			}
		} else {
			// Insertion
			Groupe group = new Groupe();
			group.setLibelle(libelleField.getText());
			group.setCode(codeField.getText());
			try {
				String res = mainApp.getWebGate().add(group);
				Groupe g = (Groupe) mainApp.getWebGate().getObjectFromJson(res, Groupe.class);
				mainApp.getWebGate().getList(Groupe.class).add(g);
				showGroup(g);
			} catch (Exception e) {
				GraphicUtils.showException(e);
			}
		}
	}

	@FXML
	void handleNew(ActionEvent event) {
		showGroup(null);
		groupList.getSelectionModel().clearSelection();
	}

	@FXML
	void handleDelete(ActionEvent event) {

		int selInxdex = groupList.getSelectionModel().getSelectedIndex();
		Groupe selectedGroup = groupList.getSelectionModel().getSelectedItem();
		if (selInxdex >= 0) {
			boolean response = gUtils.showDialog("Suppression", "Supprimer un groupe ?",
					"Voulez-vous vraiment supprimer le groupe '" + selectedGroup.getLibelle() + "' ?");
			if (response) {
				groupList.getItems().remove(selInxdex);
				try {
					mainApp.getTaskQueue().delete(selectedGroup, selectedGroup.getId());
				} catch (Exception e) {
					GraphicUtils.showException(e);
				}
			}
		} else {
			new GraphicUtils(this.mainApp).showDialog("Erreur", "", "Veuillez selectionner un groupe");
		}
	}

	/// Quizz Related Stuff///
	@FXML
	void handleEditQuizz(ActionEvent event) {
		quizzActualIncludedList.getItems().clear();
		quizzIncludedList.getItems().clear();

		tabPane.getSelectionModel().select(1);
		// Liste des Quizz dans le groupe
		ObservableList<Questionnaire> actualQuizz = FXCollections
				.observableArrayList(groupList.getSelectionModel().getSelectedItem().getQuestionnaires());

		// Instancie une liste contenant la liste de tous les autres Quizz
		ObservableList<Questionnaire> otherQuizz = FXCollections.observableArrayList(this.allQuizz);

		otherQuizz.removeAll(actualQuizz);

		quizzActualIncludedList.setItems(actualQuizz);
		quizzIncludedList.setItems(otherQuizz);

		quizzIncludedColumn.setCellValueFactory((CellDataFeatures<Questionnaire, String> feature) -> {
			Questionnaire quizz = feature.getValue();
			return new SimpleObjectProperty<>(quizz.getLibelle());
		});

		quizzActualIncludedColumn.setCellValueFactory((CellDataFeatures<Questionnaire, String> feature) -> {
			Questionnaire quizz = feature.getValue();
			return new SimpleObjectProperty<>(quizz.getLibelle());
		});
	}

	@FXML
	void handleQuizzAdd(ActionEvent event) {
		// Créer un quizz et un groupe pour les insérer dans
		// Groupe_questionnaire...
		Questionnaire quizz = quizzIncludedList.getSelectionModel().getSelectedItem();
		Groupe group = groupList.getSelectionModel().getSelectedItem();
		Groupe_questionnaire gq = new Groupe_questionnaire();
		gq.setIdGroupe(group.getId());
		gq.setIdQuestionnaire(quizz.getId());

		// Puis l'envoie sur la base de donnée en mettant à jour les trois
		// listes sur le layout plus la liste des groupes.
		try {
			String res = mainApp.getWebGate().add(gq);
			Groupe_questionnaire g = (Groupe_questionnaire) mainApp.getWebGate().getObjectFromJson(res,
					Groupe_questionnaire.class);
			mainApp.getWebGate().getList(Groupe_questionnaire.class).add(g);
			quizzActualIncludedList.getItems().add(quizz);
			quizzList.getItems().add(quizz);
			quizzIncludedList.getItems().remove(quizz);
			groupList.getSelectionModel().getSelectedItem().addQuestionnaire(quizz);
		} catch (Exception e) {
			GraphicUtils.showException(e);
		}
	}

	@FXML
	void handleQuizzDelete(ActionEvent event) {
		Groupe group = groupList.getSelectionModel().getSelectedItem();
		Questionnaire quizz = quizzActualIncludedList.getSelectionModel().getSelectedItem();

		// Créer la relation
		Groupe_questionnaire relation = new Groupe_questionnaire();
		relation.setIdGroupe(group.getId());
		relation.setIdQuestionnaire(quizz.getId());
		// Met à jour les listes
		quizzActualIncludedList.getItems().remove(quizz);
		quizzIncludedList.getItems().add(quizz);
		quizzList.getItems().remove(quizz);
		groupList.getSelectionModel().getSelectedItem().removeQuestionnaire(quizz);
		// Supprime la relation
		try {
			mainApp.getTaskQueue().deleteRelation(relation, group.getId(), quizz.getId());
		} catch (Exception e) {
			GraphicUtils.showException(e);
		}

	}

	/// User Related Stuff///
	@FXML
	void handleEditUser(ActionEvent event) {
		tabPane.getSelectionModel().select(2);

		// Liste des Quizz dans le groupe
		ObservableList<Utilisateur> actualUsers = FXCollections
				.observableArrayList(groupList.getSelectionModel().getSelectedItem().getUtilisateurs());

		userActualIncludedList.setItems(actualUsers);

		// Liste de tous les autres Quizz
		ObservableList<Utilisateur> otherUsers = FXCollections.observableArrayList(this.allUsers);

		// Exclu les utilisateurs du groupe de la liste de tous les groupes et
		// remplis la liste.
		userIncludedList.setItems(otherUsers);
		userIncludedList.getItems().removeAll(actualUsers);

		userIncludedColumn.setCellValueFactory((CellDataFeatures<Utilisateur, String> feature) -> {
			Utilisateur user = feature.getValue();
			return new SimpleObjectProperty<>(user.getNom() + " " + user.getPrenom());
		});

		userActualIncludedColumn.setCellValueFactory((CellDataFeatures<Utilisateur, String> feature) -> {
			Utilisateur user = feature.getValue();
			return new SimpleObjectProperty<>(user.getNom() + " " + user.getPrenom());
		});
	}

	@FXML
	void handleUserAdd(ActionEvent event) {
		// Créer un quizz et un groupe pour les insérer dans
		// Groupe_questionnaire...
		Utilisateur user = userIncludedList.getSelectionModel().getSelectedItem();
		Groupe group = groupList.getSelectionModel().getSelectedItem();
		Groupe_utilisateur gu = new Groupe_utilisateur();
		gu.setIdGroupe(group.getId());
		gu.setIdUtilisateur(user.getId());

		// Puis l'envoie sur la base de donnée en mettant à jour les trois
		// listes sur le layout plus la liste des groupes.
		try {
			String res = mainApp.getWebGate().add(gu);
			Groupe_utilisateur g = (Groupe_utilisateur) mainApp.getWebGate().getObjectFromJson(res,
					Groupe_utilisateur.class);
			mainApp.getWebGate().getList(Groupe_utilisateur.class).add(g);
			userActualIncludedList.getItems().add(user);
			userList.getItems().add(user);
			userIncludedList.getItems().remove(user);
			groupList.getSelectionModel().getSelectedItem().addUtilisateur(user);
		} catch (Exception e) {
			GraphicUtils.showException(e);
		}
	}

	@FXML
	void handleUserDelete(ActionEvent event) {
		Groupe group = groupList.getSelectionModel().getSelectedItem();
		Utilisateur user = userActualIncludedList.getSelectionModel().getSelectedItem();

		// Créer la relation
		Groupe_utilisateur relation = new Groupe_utilisateur();
		relation.setIdGroupe(group.getId());
		relation.setIdUtilisateur(user.getId());
		// Met à jour les listes
		userActualIncludedList.getItems().remove(user);
		userIncludedList.getItems().add(user);
		userList.getItems().remove(user);
		groupList.getSelectionModel().getSelectedItem().removeUtilisateur(user);
		// Supprime la relation
		try {
			mainApp.getWebGate().deleteRelation(relation.getClass(), user.getId(), group.getId());
		} catch (Exception e) {
			GraphicUtils.showException(e);
		}

	}

	@FXML
	void handleCancel(ActionEvent event) {
		tabPane.getSelectionModel().select(0);
	}

}
