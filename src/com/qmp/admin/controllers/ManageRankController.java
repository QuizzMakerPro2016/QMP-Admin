package com.qmp.admin.controllers;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Rang;
import com.qmp.admin.models.Utilisateur;
import com.qmp.admin.utils.Notifier;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ManageRankController extends Controller{


    @FXML
    private TableView<Rang> rankList;

    @FXML
    private TableColumn<Rang, String> libelleColumn;
    
    @FXML
    private TextField idField;
    
    @FXML
    private TextField libelleField;

    @FXML
    private TableView<Utilisateur> userList;

    @FXML
    private TableColumn<Utilisateur, String> userColumn;
    
    
    @Override
    public void setMainApp(MainApp mainApp) throws ClientProtocolException, IOException {
    	super.setMainApp(mainApp);
    	ObservableList<Rang> rankObs = mainApp.getWebGate().getList(Rang.class);
    	rankList.setItems(rankObs);
    }

    @FXML
	private void initialize() {
    		
		libelleColumn.setCellValueFactory((CellDataFeatures<Rang, String> feature) -> {
			Rang rang = feature.getValue();
			return new SimpleObjectProperty<>(rang.getLibelle());
		});
	
		userColumn.setCellValueFactory((CellDataFeatures<Utilisateur, String> feature) -> {
			Utilisateur user = feature.getValue();
			return new SimpleObjectProperty<>(user.getNom()+" "+user.getPrenom());
		});
		
		showRank(null);
		rankList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showRank(newValue));	
	
		addFieldsToCheck(libelleField);
	}
    
    public void showRank(Rang rank){
    	if(rank == null){
    		idField.setText("0");
    		libelleField.setText("");
    	}else{
    		idField.setText(String.valueOf(rank.getId()));
    		libelleField.setText(rank.getLibelle());
    		userList.setItems(FXCollections.observableArrayList(rank.getUtilisateurs()));
    	}
    }
    
    @FXML
    void handleSave(ActionEvent event) {
    	if(!checkFields())
			return;
		
		Rang rank = rankList.getSelectionModel().getSelectedItem();
		
		if(rank == null)
			rank = new Rang();
		
		if(Integer.valueOf(idField.getText()) > 0)
			rank.setId(Integer.valueOf(idField.getText()));
		
		rank.setLibelle(libelleField.getText());
		
		if(rank.getId() > 0){
			rank = (Rang) updateObject(rank, rank.getId());
			if(rank != null){
				mainApp.getTaskQueue().getAll(Rang.class);
				showRank(rank);
			}
		}else{
			rank = (Rang) addObject(rank);
			if(rank != null){
				mainApp.getWebGate().getList(Rang.class).add(rank);
				showRank(rank);
			}			
		}		
    }
    
    @FXML
    void handleNew(ActionEvent event) {
    	showRank(null);
    	rankList.getSelectionModel().clearSelection();
    }
    
    @FXML
    void handleDelete(ActionEvent event) {

    	Rang selectedRank = rankList.getSelectionModel().getSelectedItem();
		if (selectedRank != null) {
			boolean response = gUtils.showDialog("Suppression", "Supprimer un rang ?",
					"Voulez-vous vraiment supprimer le rang '" + selectedRank.getLibelle() + "' ?");
			if (response) {
				Boolean o = deleteObject(selectedRank, selectedRank.getId());
				if(o){
					mainApp.getWebGate().getList(Rang.class).remove(selectedRank);
				}else{
					Notifier.notifyWarning("Impossible de supprimer le rang", "Le rang est-il affecté à des utilisateurs ?");
				}
			}
		} else {
			Notifier.notifyWarning("Attention", "Aucun rang sélectionné.");
		}
    }
}
