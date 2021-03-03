package com.github.main;

import com.github.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.github.services.DataStorageService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {
      
      @FXML
      public Button mainExitButton;
      public Button bottomExitButton;
      public Button logoffButton;
      public Button fleetButton;
      public Button transactionsButton;
      public Text nameText;
      public Text phoneText;
      
      @FXML
      public void onActionExitButton(ActionEvent actionEvent){
      
            Stage stage = (Stage) mainExitButton.getScene().getWindow();
            stage.close();
      }
      public void onActionFleetButton(ActionEvent actionEvent) throws IOException {
            
            enterFleet();
      }
      public void onActionTransactionsButton(ActionEvent actionEvent) throws IOException {
            
            enterTransactions();
      }
      public void onActionLogoffButton(ActionEvent actionEvent) throws IOException {
            
            enterLogin();
      }
      
      
      private void enterFleet() throws IOException {
            Main.enterFleet((Stage) fleetButton.getScene().getWindow());
      }
      private void enterTransactions() throws IOException {
            Main.enterTransactions((Stage) transactionsButton.getScene().getWindow());
      }
      private void enterLogin() throws IOException {
      
            Main.enterLogin((Stage) mainExitButton.getScene().getWindow());
      
      
      }


      @Override
      public void initialize(URL url, ResourceBundle resourceBundle) {
            nameText.setText(Main.USER.getUsername());
            try {
                  for(User u: DataStorageService.loadUsers()){
                        if(u.getUsername().equals(Main.USER) && u.getPassword().equals(Main.USER.getPassword())){
                              phoneText.setText(u.getPhone());
                        }
                  }
            } catch (IOException e) {
                  e.printStackTrace();
            }

      }
}
