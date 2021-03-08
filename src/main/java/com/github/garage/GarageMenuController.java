package com.github.garage;

import com.github.entities.Car;
import com.github.services.DataStorageService;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class GarageMenuController implements Initializable {

    public Pane anchorPane;
    public Button searchButton;
    public Button sellButton;
    public Button refreshButton;
    public Button clearButton;
    protected static Car focusedCar;

    public void onActionSearchButton(ActionEvent actionEvent){

        List<Car> results = DataStorageService.findCars(focusedCar.getBrand(), focusedCar.getModel(), null, null);

    }
    public void onActionSellButton(ActionEvent actionEvent){
        System.out.println("sell");
    }
    public void onActionRefreshButton(ActionEvent actionEvent){
        System.out.println("refresh");
    }
    public void onActionClearButton(ActionEvent actionEvent){
        System.out.println("clear");
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        anchorPane.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
                    Stage menu = (Stage) anchorPane.getScene().getWindow();
                    menu.close();
        });
    }

}
