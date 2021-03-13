package com.github.transactions;

import com.github.entities.Car;
import com.github.entities.Transaction;
import com.github.entities.User;
import com.github.main.Main;
import com.github.services.DataStorageService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

public class newTransactionController implements Initializable {


    public Button sellButton;
    public Button exitButton;
    public MenuButton brandMenu = new MenuButton();
    public MenuButton modelMenu = new MenuButton();
    public MenuButton valueMenu = new MenuButton();
    public MenuButton mileageMenu = new MenuButton();
    public Pane anchorPane;

    public static Boolean finished = true;

    Set<String> brands = new HashSet<>();
    Set<String> models = new HashSet<>();
    Set<Double> values = new HashSet<>();
    Set<Integer> mileages = new HashSet<>();

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
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
            finished = true;

        }
        catch (NullPointerException | ParseException e){
            Main.showInvalidEntryPopUp();
        }
    }


    private Set<String> loadBrands(){
        List<Car> cars = new ArrayList<>();
        Set<String> brands = new HashSet<>();

        cars.addAll(DataStorageService.findCars(null, null, null, null));


        for (Car a: cars){
            brands.add(a.getBrand());
        }


        return brands;
    }
    private Set<String> loadModels(String brand){
        Set<String> models = new HashSet<>();
        for(Car c: DataStorageService.findCars(brand, null, null, null)){
            models.add(c.getModel());
        }
        return models;
    }
    private Set<Double> loadValues(String brand, String model){

        Set<Double> values = new HashSet<>();

        for(Car c: DataStorageService.findCars(brand, model, null, null)) {
            values.add(c.getValue());
        }

        return values;
    }
    private Set<Integer> loadMileages(String brand, String model, Double value){

        Set<Integer> mileages = new HashSet<>();

        for(Car c: DataStorageService.findCars(brand, model, value, null)){
            mileages.add(c.getMileage());
        }

        return mileages;
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

    private void resetAttributes(){
        brand = null;
        model = null;
        value = null;
        mileage = null;
    }

    private void initBrandMenu(){
        brandMenu.showingProperty().addListener(listener -> {

            brandMenu.getItems().clear();
            brands.clear();
            modelMenu.getItems().clear();
            models.clear();
            valueMenu.getItems().clear();
            values.clear();
            mileageMenu.getItems().clear();
            mileages.clear();

            loadBrands().forEach(brand -> brands.add(brand));
            for (String brand: brands){
                brandMenu.getItems().add(new MenuItem(brand));
            }
            brandMenu.getItems().forEach(item -> item.setOnAction(event -> {
                brandMenu.setText(item.getText());
                brand = brandMenu.getText();
            }));
        });

    }
    private void initModelMenu(){
        valueMenu.setText("Value");
        if(brandMenu.getText() != "Brand"){
            modelMenu.showingProperty().addListener(listener -> {

                modelMenu.getItems().clear();
                models.clear();
                valueMenu.getItems().clear();
                values.clear();
                mileageMenu.getItems().clear();
                mileages.clear();

                loadModels(brandMenu.getText()).forEach(model -> models.add(model));
                for (String model : models) {
                    modelMenu.getItems().add(new MenuItem(model));
                }
                modelMenu.getItems().forEach(item -> item.setOnAction(event -> {
                    modelMenu.setText(item.getText());
                    model = modelMenu.getText();
                }));

            });
        }
    }
    private void initValueMenu(){

        mileageMenu.setText("Mileage");

        if(modelMenu.getText() != "Model"){
            valueMenu.showingProperty().addListener(listener -> {

                valueMenu.getItems().clear();
                values.clear();
                mileageMenu.getItems().clear();
                mileages.clear();

                loadValues(brandMenu.getText(), modelMenu.getText()).forEach(value -> values.add(value));
                for (Double value : values) {
                    valueMenu.getItems().add(new MenuItem(value.toString()));
                }
                valueMenu.getItems().forEach(item -> item.setOnAction(event -> {
                    valueMenu.setText(item.getText());
                    value = valueMenu.getText();
                }));

            });
        }
    }
    private void initMileageMenu(){


        if(valueMenu.getText() != "Value"){
            mileageMenu.showingProperty().addListener(listener -> {

                mileageMenu.getItems().clear();
                mileages.clear();

                loadMileages(brandMenu.getText(), modelMenu.getText(), Double.parseDouble(valueMenu.getText())).forEach(mileage -> mileages.add(mileage));
                for (Integer mileage : mileages) {
                    mileageMenu.getItems().add(new MenuItem(mileage.toString()));
                }
                mileageMenu.getItems().forEach(item -> item.setOnAction(event -> {
                    mileageMenu.setText(item.getText());
                    mileage = mileageMenu.getText();
                }));
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        finished = false;

        sellButton.setDefaultButton(true);
        anchorPane.setOnMousePressed(pressEvent -> {
            anchorPane.setOnMouseDragged(dragEvent -> {
                anchorPane.getScene().getWindow().setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                anchorPane.getScene().getWindow().setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        }); /// make window draggable

        resetAttributes();

        /// menu behavior
        initBrandMenu();
        initModelMenu();
        initValueMenu();
        initMileageMenu();






    }
}
