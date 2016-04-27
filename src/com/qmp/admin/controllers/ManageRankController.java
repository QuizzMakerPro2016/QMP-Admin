package com.qmp.admin.controllers;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Rang;
import com.qmp.admin.models.Utilisateur;
import com.qmp.admin.utils.GraphicUtils;
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
    	
    	int selInxdex = rankList.getSelectionModel().getSelectedIndex();
		if (selInxdex >= 0) {
			//Update
			Rang selectedRank = rankList.getSelectionModel().getSelectedItem();
			selectedRank.setLibelle(libelleField.getText());
			try {
				String res = mainApp.getWebGate().update(selectedRank, selectedRank.getId());
				checkResult(Rang.class, res, "Rang '{{object}}' mis à jour.");
				mainApp.getTaskQueue().getAll(Rang.class);
			} catch (Exception e) {
				GraphicUtils.showException(e);
			}
		} else {
			//Insertion
			Rang rank = new Rang();
			rank.setLibelle(libelleField.getText());
			try {
				String res = mainApp.getWebGate().add(rank);
				Rang r = (Rang) checkResult(Rang.class, res, "Rang '{{object}}' ajouté.");
				if(r != null){
					mainApp.getWebGate().getList(Rang.class).add(r);
					showRank(r);
				}
			} catch (Exception e) {
				GraphicUtils.showException(e);
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

    	int selInxdex = rankList.getSelectionModel().getSelectedIndex();
		Rang selectedRank = rankList.getSelectionModel().getSelectedItem();
		if (selInxdex >= 0) {
			boolean response = gUtils.showDialog("Suppression", "Supprimer un rang ?", "Voulez-vous vraiment supprimer le rang '" + selectedRank.getLibelle() + "' ?");
			if(response){
				rankList.getItems().remove(selInxdex);
				try {
					String res  = mainApp.getWebGate().delete(selectedRank, selectedRank.getId());
					checkResult(Rang.class, res, "Rang '{{object}}' supprimé.");
				} catch (Exception e) {
					GraphicUtils.showException(e);
				}
			}
		} else {
			Notifier.notifyWarning("Attention", "Aucun rang sélectionné.");
		}
    }
}
