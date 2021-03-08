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
    public static final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public static List<User> loadUsers() throws IOException {

        return entityManager.createNativeQuery("SELECT * FROM `users`", User.class).getResultList();

    }
    public static void addUser(User user) throws IOException {

        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();

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
    public static List<Car> findCars(String brand, String model, Double value, Integer mileage){
        if(brand != null && model != null && value != null && mileage != null){
            return entityManager.createQuery("select c from Car c where c.brand = ?0 " +
                    "and c.model = ?1 " +
                    "and c.value = ?2 " +
                    "and c.mileage = ?3", Car.class)
                    .setParameter(0, brand)
                    .setParameter(1, model)
                    .setParameter(2, value)
                    .setParameter(3, mileage)
                    .getResultList();

        }
        else if(brand != null && model != null && value != null){
            return entityManager.createQuery("select c from Car c where c.brand = ?0 " +
                    "and c.model = ?1 " +
                    "and c.value = ?2", Car.class)
                    .setParameter(0, brand)
                    .setParameter(1, model)
                    .setParameter(2, value)
                    .getResultList();
        }
        else if(brand != null && model != null){
            return entityManager.createQuery("select c from Car c where c.brand = ?0 " +
                    "and c.model = ?1", Car.class)
                    .setParameter(0, brand)
                    .setParameter(1, model)
                    .getResultList();
        }
        else{
            return entityManager.createQuery("select c from Car c where c.brand = ?0 " +
                    "and c.model = ?1", Car.class)
                    .setParameter(0, brand)
                    .getResultList();
        }

    }
    public static ObservableList<Car> searchCars(String text) {

        return FXCollections.observableArrayList(entityManager.createNativeQuery("select * from cars where brand like '%" + text + "%'" +
                "or model like '%" + text + "%'", Car.class)
                .getResultList());
    }


    public static ObservableList<Transaction> searchTransactions(String text) {

        return FXCollections.observableArrayList(entityManager.createNativeQuery("select * from transactions where brand like '%" + text + "%'" +
                "or model like '%" + text + "%'", Transaction.class)
                .getResultList());
    }
    public static void addTransaction(Transaction transaction) throws IOException, ParseException {

        entityManager.getTransaction().begin();
        entityManager.persist(transaction);
        entityManager.getTransaction().commit();



    }
    public static void removeTransaction(Transaction transaction) throws IOException {

        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM Transaction WHERE id = :id")
                .setParameter("id", transaction.getId())
                .executeUpdate();
        entityManager.persist(new Car(null, transaction.getCarBrand(), transaction.getCarModel(), transaction.getCarValue(), transaction.getCarMileage()));
        entityManager.getTransaction().commit();

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

