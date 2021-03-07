package com.github.transactions;

import com.github.entities.Car;
import com.github.entities.Transaction;
import com.github.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
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


    public Button sellButton;
    public Button exitButton;
    public MenuButton brandMenu;
    public MenuButton modelMenu;
    public MenuButton valueMenu;
    public MenuButton mileageMenu;
    public Pane anchorPane;

    private String brand;
    private String model;
    private String value;
    private String mileage;


    public void onActionExitButton(ActionEvent actionEvent){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    public void onActionSellButton(ActionEvent actionEvent) throws IOException {
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

    public void onActionBrand(ActionEvent actionEvent){

    }
    public void onActionModel(ActionEvent actionEvent){

    }
    public void onActionValue(ActionEvent actionEvent){

    }
    public void onActionMileage(ActionEvent actionEvent){

    }

    private Transaction processTransaction() throws IOException {

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        User salesman = DataStorageService.entityManager.createQuery("select u from User u where u.username = ?0 and u.password = ?1", User.class)
                .setParameter(0, Main.USER.getUsername())
                .setParameter(1, Main.USER.getPassword()).getResultList().get(0);


        Car car = DataStorageService.entityManager.createQuery("select c from Car c where c.brand = ?0 and c.model = ?1 and c.value = ?2 and c.mileage = ?3", Car.class)
                .setParameter(0, brand)
                .setParameter(1, model)
                .setParameter(2, Double.parseDouble(value))
                .setParameter(3, Integer.parseInt(mileage))
                .getResultList().get(0);

        DataStorageService.removeCar(car);

        return new Transaction(null, car, date, salesman);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        sellButton.setDefaultButton(true);

        anchorPane.setOnMousePressed(pressEvent -> {
            anchorPane.setOnMouseDragged(dragEvent -> {
                anchorPane.getScene().getWindow().setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                anchorPane.getScene().getWindow().setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });

    }
}
