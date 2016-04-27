package com.qmp.admin.controllers;

import com.qmp.admin.models.Questionnaire;
import com.qmp.admin.models.Utilisateur;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class HomeController extends Controller{

    @FXML
    private TableView<Utilisateur> userList;

    @FXML
    private TableColumn<Utilisateur, String> userColumn;

    @FXML
    private TableView<Questionnaire> quizzList;

    @FXML
    private TableColumn<Questionnaire, String> quizzColumn;
}
