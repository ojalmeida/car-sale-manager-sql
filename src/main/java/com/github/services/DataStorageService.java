package com.github.services;

import com.github.entities.Car;
import com.github.entities.Transaction;
import com.github.entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataStorageService implements DataStorageInterface{

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence");
    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public static List<User> loadUsers() throws IOException {

        return entityManager.createNativeQuery("SELECT * FROM `users`", User.class).getResultList();

    }
    public static void addUser(User user) throws IOException {

        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();

    }

    public static String encryptData(String data) {

        char[] decrypted_chars = data.toCharArray();
        StringBuilder encrypted_data = new StringBuilder();

        for (int i = 0; i < decrypted_chars.length; i++){
            char[] encrypted_chars = new char[decrypted_chars.length];
            encrypted_chars[i] = (char) (decrypted_chars[i] + 1);
            encrypted_data.append((encrypted_chars[i]));
        }

        return encrypted_data.toString();
    }
    public static String decryptData(String encryptedData) {
        char[] encrypted_chars = encryptedData.toCharArray();
        StringBuilder decrypted_data = new StringBuilder();

        for (int i = 0; i < encrypted_chars.length; i++){
            char[] decrypted_chars = new char[encrypted_chars.length];
            decrypted_chars[i] = (char) (encrypted_chars[i] - 1);
            decrypted_data.append((decrypted_chars[i]));
        }

        return decrypted_data.toString();
    }

    public static ObservableList<Car> cars() throws IOException {

        verifyFiles();

        return FXCollections.observableArrayList(entityManager.createNativeQuery("SELECT * FROM `cars`", Car.class).getResultList());
    } // loads the cars from the file
    public static ObservableList<Transaction> transactions() throws IOException, ParseException {

        verifyFiles();

        return FXCollections.observableArrayList(entityManager.createNativeQuery("SELECT * FROM `transactions`", Transaction.class).getResultList());


    } //loads transactions from the file

    public static void addCar(Car car) throws IOException {

        entityManager.getTransaction().begin();
        entityManager.persist(car);
        entityManager.getTransaction().commit();

    }
    public static void removeCar(Car car) throws IOException {

        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM Car WHERE id = :id")
                .setParameter("id", car.getId())
                .executeUpdate();
        entityManager.getTransaction().commit();
    }

    public static void addTransaction(Transaction transaction) throws IOException, ParseException {

        entityManager.getTransaction().begin();
        entityManager.persist(transaction);
        entityManager.getTransaction().commit();



    }
    public static void removeTransactionFromFile(Transaction transaction) throws IOException {


    }

    private static void verifyFiles() {
        if(entityManager.createNativeQuery("SHOW TABLES LIKE 'users'").getResultList().size() == 0){
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("CREATE TABLE 'users'");
            entityManager.getTransaction().commit();
        }
        else if(entityManager.createNativeQuery("SHOW TABLES LIKE 'cars'").getResultList().size() == 0){
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("CREATE TABLE 'cars'");
            entityManager.getTransaction().commit();
        }
        else if(entityManager.createNativeQuery("SHOW TABLES LIKE 'transactions'").getResultList().size() == 0){
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("CREATE TABLE 'transactions'");
            entityManager.getTransaction().commit();
        }
    }
    public static Boolean verifyUserData(User user) throws IOException {
        for (User u : DataStorageService.loadUsers()){
            if (u.getUsername().equals(user.getUsername()) &&
                    u.getPassword().equals(user.getPassword())){
                return true;
            }
        }
        return false;
    }


}

