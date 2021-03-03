package com.github.transactions;

import com.github.entities.Car;
import com.github.entities.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.github.main.Main;
import com.github.services.DataStorageService;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class newTransactionController implements Initializable {

    @FXML
    public TextField brandField;
    public TextField modelField;
    public TextField valueField;
    public TextField mileageField;
    public Button goButton;
    public Button exitButton;
    public MenuButton menuButton;
    public MenuItem buy;
    public MenuItem sale;

    private String brand;
    private String model;
    private String value;
    private String mileage;


    public static Boolean isBuying = false;
    public static Boolean isSelling = false;
    public void buyOptionSelected(ActionEvent actionEvent){
        menuButton.setText("Buy");
        isSelling = false;
        isBuying = true;
    }
    public void saleOptionSelected(ActionEvent actionEvent){
        menuButton.setText("Sale");
        isSelling = true;
        isBuying = false;
    }


    public void onActionExitButton(ActionEvent actionEvent){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    public void onActionGoButton(ActionEvent actionEvent) throws IOException {
        try {
            DataStorageService.addTransaction(processTransaction());
            TransactionsController.NEEDS_DATA_UPDATE.set(true);
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();

        }
        catch (NullPointerException | ParseException e){
            Main.showInvalidEntryPopUp();
        }
    }

    private Transaction processTransaction(){

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        if(isBuying){
            Car car = new Car(brand, model, Double.parseDouble(value) * (-1), Integer.parseInt(mileage));
            return new Transaction(car, date, Main.USER);
        }
        else{
            Car car = new Car(brand, model, Double.parseDouble(value), Integer.parseInt(mileage));
            return new Transaction(car, date, Main.USER);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        brandField.textProperty().addListener((observable, oldValue, newValue) -> {

            brand = newValue;

        });
        modelField.textProperty().addListener((observable, oldValue, newValue) -> model = newValue);
        valueField.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.matches("\\d*")) {
                valueField.setText(newValue.replaceAll("[^\\d]", ""));
            }

            value = newValue;
        });
        mileageField.textProperty().addListener((observable, oldValue, newValue) -> {

                if (!newValue.matches("\\d*")) {
                    mileageField.setText(newValue.replaceAll("[^\\d]", ""));
                }

                mileage = newValue;
            });

        goButton.setDefaultButton(true);

    }
}
