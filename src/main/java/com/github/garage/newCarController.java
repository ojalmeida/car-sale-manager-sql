package com.github.garage;

import com.github.entities.Car;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.github.services.DataStorageService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class newCarController implements Initializable {

    public static Boolean isReady = false;
    public static Boolean wasClosed = false;

    @FXML
    public Button insertButton;
    public Button exitButton;
    public TextField brandTextField;
    public TextField modelTextField;
    public TextField valueTextField;
    public TextField mileageTextField;
    public Pane anchorPane;

    static String brand = null;
    static String model = null;
    static Double value = 0.0;
    static Integer mileage = 0;

    public static Car newCar;


    public void onActionExitButton(ActionEvent actionEvent){

        wasClosed = true;
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

    }
    public void onActionInsertButton(ActionEvent actionEvent) throws IOException {

        newCar = new Car(null, brand, model, value, mileage);
        DataStorageService.addCar(newCar);
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
        isReady = true;


    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        isReady = false;
        wasClosed = false;


        brandTextField.textProperty().addListener((observable, oldValue, newValue) -> brand = newValue);
        modelTextField.textProperty().addListener((observable, oldValue, newValue) -> model = newValue);
        valueTextField.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.matches("\\d*")) {
                valueTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }

            value = Double.valueOf(newValue);
        });
        mileageTextField.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.matches("\\d*")) {
                mileageTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }

            mileage = Integer.valueOf(newValue);
        });

        anchorPane.setOnMousePressed(pressEvent -> {
            anchorPane.setOnMouseDragged(dragEvent -> {
                anchorPane.getScene().getWindow().setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                anchorPane.getScene().getWindow().setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });

    }
}
