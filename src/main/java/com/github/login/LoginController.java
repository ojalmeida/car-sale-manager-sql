package com.github.login;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.github.main.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {


    public Button exitButton;
    public Button loginButton;
    public Button SignUpButton;
    public TextField userTextField = new TextField();
    public PasswordField passwordField = new PasswordField();


    public void onActionExitButton(ActionEvent actionEvent){

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    public void onActionLoginButton(ActionEvent actionEvent) throws IOException {
        enterMain();
    }
    public void onActionSignUpButton(ActionEvent actionEvent) throws IOException {
        enterSignUp();
    }

    private void enterMain() throws IOException {
        Main.enterMain((Stage) exitButton.getScene().getWindow());
    }
    private void enterSignUp() throws IOException {
        Main.enterSignUp((Stage) exitButton.getScene().getWindow());
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Main.reloadUser();

        String usernametyped = "";
        String passwordtyped = "";

        userTextField.textProperty().addListener((observable, oldValue, newValue) -> {

            Main.USER.setUsername(usernametyped.concat(newValue));
        });
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {

            Main.USER.setPassword(passwordtyped.concat(newValue));
        });

        loginButton.setDefaultButton(true);
    }
}
