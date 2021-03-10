package com.github.garage;

import com.github.entities.Car;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.github.main.Main;
import com.github.services.DataStorageService;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class GarageController implements Initializable {


    @FXML
    public Button exitButton;
    public Button insertButton;
    public Button removeButton;
    public Button backButton;
    public TextField searchTextField;
    public TableView<Car> tableView;
    public TableColumn<Car, String> brand;
    public TableColumn<Car, String> model;
    public TableColumn<Car, Double> value;
    public TableColumn<Car, Integer> mileage;

    private static Boolean onMenu = false;
    private static Boolean onNewCar = false;

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

        onMenu = false;
        onNewCar = true;
        new UpdateService(tableView).start();

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

        } catch (IOException e) {
            System.out.println("An error occurred");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        brand.setCellValueFactory(new PropertyValueFactory<>("Brand"));
        model.setCellValueFactory(new PropertyValueFactory<>("Model"));
        value.setCellValueFactory(new PropertyValueFactory<>("Value"));
        mileage.setCellValueFactory(new PropertyValueFactory<>("Mileage"));

        updateTable();

        tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            if (mouseEvent.getButton() == MouseButton.SECONDARY && tableView.getSelectionModel().getSelectedItem() != null){

                GarageMenuController.focusedCar = tableView.getSelectionModel().getSelectedItem();
                Stage menu = new Stage();
                try {
                    menu.setScene(new Scene(FXMLLoader.load(Main.class.getResource("/fxml/garage_menu.fxml"))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                menu.initStyle(StageStyle.UNDECORATED);
                menu.setY(mouseEvent.getSceneY());
                menu.setX(mouseEvent.getSceneX() + 75);
                menu.show();

                onNewCar = false;
                onMenu = true;
                new UpdateService(tableView).start();
            }
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            tableView.getItems().setAll(DataStorageService.searchCars(newValue));
        });

    }

    public static class UpdateService extends Service<Void>{

        protected UpdateService(TableView<Car> tableView){
            setOnSucceeded(workerStateEvent -> {
                if(newCarController.finished){ ///update table
                    try {
                        tableView.getItems().setAll(DataStorageService.cars());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (GarageMenuController.hasResults){
                    tableView.getItems().setAll(FXCollections.observableArrayList(GarageMenuController.results));
                }
                else if(GarageMenuController.needClear){
                    for(Car c: tableView.getItems()){
                        try {
                            DataStorageService.removeCar(c);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    tableView.getItems().clear();
                }
            });
        }

        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception {
                    if(onNewCar) {
                        while (!newCarController.finished) {
                            Thread.sleep(10);
                        }
                        return null;
                    }
                    else if (onMenu){
                        while (!GarageMenuController.finished) {
                            Thread.sleep(10);
                        }
                        return null;
                    }
                    else{
                        throw new RuntimeException("State variables error");
                    }
                }
            };
        }
    }

}