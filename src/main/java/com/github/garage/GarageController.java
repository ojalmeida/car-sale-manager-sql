package com.github.garage;

import com.github.entities.Car;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.github.main.Main;
import com.github.services.DataStorageService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GarageController implements Initializable {

    public static SimpleBooleanProperty NEEDS_DATA_UPDATE = new SimpleBooleanProperty(false);

    @FXML
    public Button exitButton;
    public Button insertButton;
    public Button removeButton;
    public Button backButton;
    public TableView<Car> tableView;
    public TableColumn<Car, String> brand;
    public TableColumn<Car, String> model;
    public TableColumn<Car, Double> value;
    public TableColumn<Car, Integer> mileage;

    @FXML
    public void onActionExitButton(ActionEvent actionEvent){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void onActionInsertButton(ActionEvent actionEvent) throws IOException {
        Stage popup = new Stage();
        popup.setScene(new Scene(FXMLLoader.load(Main.class.getResource("/fxml/newCar_scene.fxml"))));
        popup.initStyle(StageStyle.UNDECORATED);
        popup.setX(380);
        popup.setY(260);
        popup.show();

    }

    public void onActionRemoveButton(ActionEvent actionEvent) throws IOException {
        Car selectedCar = tableView.getSelectionModel().getSelectedItem();
        DataStorageService.removeCar(selectedCar);
        tableView.getItems().remove(selectedCar);

    }

    public void onActionBackButton(ActionEvent actionEvent) throws IOException {
        Main.enterMain((Stage) exitButton.getScene().getWindow());
    }

    public void updateTable(){

        try {

            tableView.getItems().setAll(DataStorageService.cars());
            NEEDS_DATA_UPDATE.set(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        brand.setCellValueFactory(new PropertyValueFactory<>("Brand"));
        model.setCellValueFactory(new PropertyValueFactory<>("Model"));
        value.setCellValueFactory(new PropertyValueFactory<>("Value"));
        mileage.setCellValueFactory(new PropertyValueFactory<>("Mileage"));

        updateTable();

        NEEDS_DATA_UPDATE.addListener((observable, oldValue, newValue) -> {

            if(newValue){

                updateTable();

            }

        });

        NEEDS_DATA_UPDATE.set(true);
    }
}