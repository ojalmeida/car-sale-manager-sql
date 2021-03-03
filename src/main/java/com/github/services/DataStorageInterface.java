package com.github.services;

import com.github.entities.Car;
import com.github.entities.Transaction;
import com.github.entities.User;
import javafx.collections.ObservableList;

import java.util.List;

public interface DataStorageInterface {

    static List<User> loadUsers(){
        return null;
    }
    static void addUser(User user){
    }

    static String encryptData(String data){
        return null;
    }
    static String decryptData(String encryptedData){
        return null;
    }

    static ObservableList<Car> carsStoraged(){
        return null;
    }
    static ObservableList<Transaction> transactions(){
        return null;
    }

    static void addCarToFile(Car car){}
    static void removeCarFromFile(Car car){}

    static void addTransaction(Transaction transaction){}
    static void removeTransactionFromFile(Transaction transaction){}

    static void verifyFiles(){}
    static Boolean verifyUserData(User user){
        return null;
    }

}
