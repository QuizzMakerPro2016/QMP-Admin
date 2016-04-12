package com.qmp.admin.utils;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GraphicUtils {
	
	public static void switchView(Object controller, Stage appStage, String viewName){
		Parent dispatcher = null;
		try {
			dispatcher = FXMLLoader.load(controller.getClass().getResource("/com/qmp/admin/views/" + viewName + ".fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Scene dispatcherScene = new Scene(dispatcher);
        appStage.setScene(dispatcherScene);
	}
	
	public static void showAlert(){
		
	}
	
	public static boolean showDialog(){
		return false;
	}

}
