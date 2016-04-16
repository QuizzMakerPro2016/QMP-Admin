package com.qmp.admin.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import com.qmp.admin.MainApp;
import com.qmp.admin.controllers.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class GraphicUtils {
	
	private static MainApp mainApp;
	
	public GraphicUtils(MainApp mainApp){
		this.mainApp = mainApp;
	}
	
	public <T> void switchView(String viewName){
		try {
			//Load center
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/com/qmp/admin/views/" + viewName + ".fxml"));
			AnchorPane domainOverview = (AnchorPane) loader.load();
			mainApp.getRootLayout().setCenter(domainOverview);
			
			Controller controller = loader.getController();
			controller.setMainApp(mainApp);
		} catch (Exception e) {
			showException(e);
			//e.printStackTrace();
		}
	}
	
	public void homeLayout(){
		try {
			//Load menu
			FXMLLoader loaderMenu = new FXMLLoader();
			loaderMenu.setLocation(MainApp.class.getResource("/com/qmp/admin/views/MainPage.fxml"));
			AnchorPane menuOverview = (AnchorPane) loaderMenu.load();
			mainApp.getRootLayout().setLeft(menuOverview);
			
			Controller controller = loaderMenu.getController();
			controller.setMainApp(mainApp);
			
			//Load home
			FXMLLoader loaderHome = new FXMLLoader();
			loaderHome.setLocation(MainApp.class.getResource("/com/qmp/admin/views/homeLayout.fxml"));
			AnchorPane homeOverview = (AnchorPane) loaderHome.load();
			mainApp.getRootLayout().setCenter(homeOverview);
		} catch (Exception e) {
			showException(e);
			//e.printStackTrace();
		}
	}
	
	
	
	public static void showAlert(){
		
	}
	
	public static void showException(Exception e){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Dialog");
		alert.setHeaderText("Look, an Exception Dialog");
		alert.setContentText(e.getMessage());

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
	
	public boolean showDialog(String title, String header, String content){
		Alert alert = new Alert(AlertType.CONFIRMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(header);
	    alert.setContentText(content);
	    addStyle(alert);
	    Optional<ButtonType> result = alert.showAndWait();
	    
		if(result.get() == ButtonType.OK)
			return true;
		
	    return false;
	}
	
	private void addStyle(Alert alert){
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(
				MainApp.class.getResource("/com/qmp/admin/views/QMP-style.css").toExternalForm());
		dialogPane.getStyleClass().add("myDialog");
	}

}

