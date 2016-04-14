package com.qmp.admin;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.qmp.admin.controllers.MainController;
import com.qmp.admin.models.Domaine;
import com.qmp.admin.models.Rang;
import com.qmp.admin.models.Utilisateur;
import com.qmp.admin.utils.GraphicUtils;
import com.qmp.admin.utils.WebGate;
import com.qmp.admin.utils.saves.SaveOperationTypes;
import com.qmp.admin.utils.saves.TaskQueue;

import javafx.application.Application;
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

    /**
	 * @return the webGate
	 */
	public WebGate getWebGate() {
		return webGate;
	}

	private Utilisateur user;
    

    private TaskQueue taskQueue;
    
    private ObservableMap<String, Object> data;
	private List<Utilisateur> list;

    /**
     * Constructor
     */
    public MainApp() {
    	super();
		webGate = new WebGate();
		taskQueue = new TaskQueue("mainFx", webGate);
		taskQueue.addObserver(this);


		webGate.getList(Utilisateur.class);
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

        } catch (Exception e) {
            GraphicUtils.showException(e);
        }
        taskQueue.start();
        
		list = webGate.getList(Utilisateur.class);
		loadLists();

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
        } catch (Exception e) {
        	GraphicUtils.showException(e);
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

    public void setUser(Utilisateur user){
    	this.user = user;
    }
    
    public Utilisateur getUser(){
    	return this.user;
    }
    public boolean isLogged(){
    	if (this.getUser() != null){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public boolean isAdmin(){
    	if (this.getUser().getRang().getLibelle().equals("admin")){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
		Object[] args = (Object[]) arg;
		if (args[0].equals(SaveOperationTypes.GET)) {
			webGate.addAll((List<Object>) args[2], (Class<Object>) args[1]);
		}
	}

	public void loadLists() {
		taskQueue.getAll(Rang.class);
		taskQueue.getAll(Utilisateur.class);
		taskQueue.getAll(Domaine.class);
	}
	
	public BorderPane getRootLayout(){
		return this.rootLayout;
	}

	public TaskQueue getTaskQueue() {
		return taskQueue;
	}
}