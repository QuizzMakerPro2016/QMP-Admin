package com.qmp.admin.controllers;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Groupe;
import com.qmp.admin.models.Questionnaire;
import com.qmp.admin.models.Rang;
import com.qmp.admin.models.Utilisateur;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class HomeController extends Controller{

    @FXML
    private TableView<Utilisateur> userList;

    @FXML
    private TableColumn<Utilisateur, String> userColumn;

    @FXML
    private TableView<Questionnaire> quizzList;

    @FXML
    private TableColumn<Questionnaire, String> quizzColumn;
    
    @FXML
    private BarChart<String, Integer> quizzBar;
    
    @FXML
    private CategoryAxis xAxis;
    
	@Override
	public void setMainApp(MainApp mainApp) throws ClientProtocolException, IOException {
		super.setMainApp(mainApp);
		
		ArrayList<Groupe> groups = (ArrayList<Groupe>) mainApp.getWebGate().getAll(Groupe.class, 0, 10, 2,"id","DESC");
		
		ObservableList<Utilisateur> userObs;
		userObs = FXCollections
				.observableArrayList(mainApp.getWebGate().getAll(Utilisateur.class, 0, 5, 2,"id","DESC"));
		userList.setItems(userObs);

		
		ObservableList<Questionnaire> quizzObs;
		quizzObs =  FXCollections
				.observableArrayList(mainApp.getWebGate().getAll(Questionnaire.class, 0, 5, 2,"id","DESC"));
		quizzList.setItems(quizzObs);

		
		ObservableList<String> obsNameGroup = FXCollections.observableArrayList();
		ObservableList<Integer> obsNumberQuizzGroup = FXCollections.observableArrayList();
		//Création de la barre
		for(Groupe g : groups){
			obsNameGroup.add(g.getLibelle());
			obsNumberQuizzGroup.add(g.getQuestionnaires().size());
		}
		XYChart.Series<String, Integer> series = new XYChart.Series<>();
		//On bind les listes aux axes
		for (int i = 0; i < obsNameGroup.size(); i++) {
            series.getData().add(new XYChart.Data<>(obsNameGroup.get(i),obsNumberQuizzGroup.get(i)));
        }
		
		//xAxis.setCategories(obsNameGroup);
		quizzBar.getData().add(series);

	}

	@FXML
	private void initialize() {

		userColumn.setCellValueFactory((CellDataFeatures<Utilisateur, String> feature) -> {
			Utilisateur user = feature.getValue();
			return new SimpleObjectProperty<>(user.getNom() + " " + user.getPrenom());
		});

		quizzColumn.setCellValueFactory((CellDataFeatures<Questionnaire, String> feature) -> {
			Questionnaire quizz = feature.getValue();
			return new SimpleObjectProperty<>(quizz.getLibelle());
		});

		userList.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
					mainApp.getTaskQueue().getAll(Rang.class);
					mainApp.getTaskQueue().getAllRefresh(Utilisateur.class, 2);
					gUtils.switchView("ManageUserLayout");
			    }
		});
		
		quizzList.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
					mainApp.getTaskQueue().getAll(Rang.class);
					mainApp.getTaskQueue().getAllRefresh(Utilisateur.class, 2);
					gUtils.switchView("QuizzHomeLayout");
			    }
		});
		
	}
}
