package com.github.alerts;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WrongCredentialsController {


    @FXML
    public Button exitButton;

    @FXML
    public void onActionExitButton(ActionEvent actionEvent){

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

    }
}

