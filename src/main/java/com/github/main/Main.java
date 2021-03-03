package com.github.main;

import com.github.entities.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.github.services.DataStorageService;

import java.io.*;

public class Main extends Application {

    public static User USER = new User();

    public static void enterLogin(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(Main.class.getResource("/fxml/login_scene.fxml"));
        stage.setTitle("Login");
        stage.setScene(new Scene(root, 1280, 720));
        stage.show();
    }
    public static void enterFleet(Stage stage) throws IOException {


        Parent root = FXMLLoader.load(Main.class.getResource("/fxml/garage_scene.fxml"));
        stage.setTitle("Fleet");
        stage.setScene(new Scene(root, 1280, 720));
        stage.show();
    }
    public static void enterTransactions(Stage stage) throws IOException {


        Parent root = FXMLLoader.load(Main.class.getResource("/fxml/transactions_scene.fxml"));
        stage.setTitle("Transactions");
        stage.setScene(new Scene(root, 1280, 720));
        stage.show();
    }
    public static void enterSignUp(Stage stage) throws IOException {


        Parent root = FXMLLoader.load(Main.class.getResource("/fxml/signup_scene.fxml"));
        stage.setTitle("Sign up");
        stage.setScene(new Scene(root, 1280, 720));
        stage.show();
    }
    public static void enterMain(Stage stage) throws IOException {

        if (DataStorageService.verifyUserData(USER)) {

            Parent root = FXMLLoader.load(Main.class.getResource("/fxml/Main_Scene.fxml"));
            stage.setTitle("Car Sale Manager");
            stage.setScene(new Scene(root, 1280, 720));
            stage.show();
        }
        else{

            showWrongCredentialsPopUp();
        }
    }
    public static void showWrongCredentialsPopUp() throws IOException {

        Stage popup = new Stage();
        popup.setScene(new Scene(FXMLLoader.load(Main.class.getResource("/fxml/wrong_credentials.fxml"))));
        popup.initStyle(StageStyle.UNDECORATED);
        popup.setX(380);
        popup.setY(260);
        popup.show();

    }
    public static void showInvalidEntryPopUp() throws IOException {

        Stage popup = new Stage();
        popup.setScene(new Scene(FXMLLoader.load(Main.class.getResource("/fxml/invalid_entry.fxml"))));
        popup.initStyle(StageStyle.UNDECORATED);
        popup.setX(380);
        popup.setY(260);
        popup.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{

        Parent root = FXMLLoader.load(Main.class.getResource("/fxml/login_scene.fxml"));
        stage.setTitle("Login");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root, 1280, 720));
        stage.show();

    }

    public static void reloadUser(){
        Main.USER.setUsername("");
        Main.USER.setPassword("");
        Main.USER.setPhone("");
    }
}
