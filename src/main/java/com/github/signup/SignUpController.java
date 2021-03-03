package com.github.signup;

import com.github.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.github.main.Main;
import com.github.services.DataStorageService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

      public Button exitButton;
      public Button createButton;
      public Button backButton;
      public TextField usernameTextField = new TextField();
      public TextField phoneTextField = new TextField();
      public PasswordField passwordField = new PasswordField();
      public PasswordField checkPasswordField = new PasswordField();
      public String usernameTyped = "";
      public String passwordTyped = "";
      public String checkPasswordTyped = "";
      public String phoneTyped = "";
      

      public void onActionExitButton(ActionEvent actionEvent){
      
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
            
      }
      public void onActionCreateButton(ActionEvent actionEvent) throws IOException {
            if(passwordTyped.equals(checkPasswordTyped)) {
                  createAccount();
            }
            else{
                  Main.showWrongCredentialsPopUp();
            }
      }
      public void onActionBackButton(ActionEvent actionEvent) throws IOException {
            enterLogin();
      }
      
      private void createAccount() throws IOException {

            DataStorageService.addUser(new User(usernameTyped, passwordTyped, phoneTyped));
            Main.USER.setUsername(usernameTyped);
            Main.USER.setPassword(passwordTyped);
            Main.USER.setPhone(phoneTyped);

            Main.enterMain((Stage) exitButton.getScene().getWindow());

      }
      private void enterLogin() throws IOException {
            Main.enterLogin((Stage) exitButton.getScene().getWindow());
      }

      @Override
      public void initialize(URL url, ResourceBundle resourceBundle) {

            Main.reloadUser();

            usernameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                  usernameTyped = newValue;
            });

            passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
                  passwordTyped = newValue;
            });

            checkPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
                  checkPasswordTyped = newValue;
            });

            phoneTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                  phoneTyped = newValue;
            });
      }
}
