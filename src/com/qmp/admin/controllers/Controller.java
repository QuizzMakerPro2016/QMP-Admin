package com.qmp.admin.controllers;

import com.qmp.admin.MainApp;
import com.qmp.admin.models.Domaine;
import com.qmp.admin.utils.GraphicUtils;

import javafx.collections.ObservableList;

public class Controller {
	
	protected MainApp mainApp;
	protected GraphicUtils gUtils;
	
    public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		gUtils = new GraphicUtils(mainApp);
	}

}
