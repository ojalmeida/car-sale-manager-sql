package com.github.garage;

import com.github.entities.Car;
import com.github.entities.Transaction;
import com.github.main.Main;
import com.github.services.DataStorageService;
import com.github.transactions.TransactionsController;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


public class GarageMenuController implements Initializable {

    public Pane anchorPane;
    public Button searchButton;
    public Button sellButton;
    public Button refreshButton;
    public Button clearButton;
    protected static Car focusedCar;
    public static List<Car> results = null;

    public static Boolean isReady = false;
    public static Boolean wasClosed = false;
    public static Boolean needClear = false;
    public static Boolean hasResults = false;

    public void onActionSearchButton(ActionEvent actionEvent){

        results = DataStorageService.findCars(focusedCar.getBrand(), focusedCar.getModel(), null, null);
        hasResults = true;
        exit();


    }
    public void onActionSellButton(ActionEvent actionEvent) throws IOException, ParseException {

        DataStorageService.addTransaction(new Transaction(null, focusedCar, new Date(), Main.USER));
        DataStorageService.removeCar(focusedCar);
        exit();

    }
    public void onActionRefreshButton(ActionEvent actionEvent){
        exit();
    }
    public void onActionClearButton(ActionEvent actionEvent){

        needClear = true;
        exit();

    }

    private void resetAttributes(){
        isReady = false;
        wasClosed = false;
        needClear =  false;
        hasResults = false;
        results = null;
    }
    private void exit(){
        wasClosed = true;
        Stage menu = (Stage) anchorPane.getScene().getWindow();
        menu.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        anchorPane.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
                    exit();
        });
        resetAttributes();
    }

}
