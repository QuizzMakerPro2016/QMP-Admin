package com.qmp.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.qmp.admin.controllers.MainController;
import com.qmp.admin.models.Utilisateur;
import com.qmp.admin.utils.WebGate;
import com.qmp.admin.utils.saves.SaveOperationTypes;
import com.qmp.admin.utils.saves.TaskQueue;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application implements Observer {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private WebGate webGate;
    private TaskQueue taskQueue;
    
    private ObservableMap<String, Object> data;

    /**
     * Constructor
     */
    public MainApp() {
    	super();
		webGate = new WebGate();
		taskQueue = new TaskQueue("mainFx", webGate);
		taskQueue.addObserver(this);

		data = FXCollections.observableHashMap();

		taskQueue.getAll(Utilisateur.class);
		taskQueue.getAll(Utilisateur.class);
    }
    
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("QuizzMakerPro 2016 Admin");

        initRootLayout();

       //Load Main Page
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/com/qmp/admin/views/ConnexionLayout.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            MainController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
        taskQueue.start();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/com/qmp/admin/views/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }


	@Override
	public void update(Observable arg0, Object arg) {
		Object[] args = (Object[]) arg;
		String key =  ((Class) args[1]).getSimpleName();
		List<Object> objects = (List<Object>) args[2];
		if (args[0].equals(SaveOperationTypes.GET) || args[0].equals(SaveOperationTypes.GET_LOCAL)) {
			List<Object> list = (List<Object>) data.get(key);
			if(list == null){
				list = new ArrayList<Object>();
				data.put(key, list);
			}else{
				if(args[0].equals(SaveOperationTypes.GET_LOCAL))
					list.clear();
			}
			((List<Object>) data.get(key)).addAll(objects);		
		}
		
	}
}