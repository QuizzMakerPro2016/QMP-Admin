package com.qmp.admin.utils;

import java.io.IOException;

import com.qmp.admin.MainApp;
import com.qmp.admin.controllers.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class GraphicUtils {
	
	public static <T> void switchView(MainApp mainApp, String viewName){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/com/qmp/admin/views/" + viewName + ".fxml"));
			AnchorPane domainOverview = (AnchorPane) loader.load();
			mainApp.getRootLayout().setCenter(domainOverview);
			Controller controller = loader.getController();
			controller.setMainApp(mainApp);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static void showAlert(){
		
	}
	
	public static boolean showDialog(){
		return false;
	}

}

