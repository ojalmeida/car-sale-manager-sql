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
    public static List<Car> findCarsByBrand(String brand){
        return entityManager.createQuery("select c from Car c where c.brand = ?0", Car.class)
                .setParameter(0, brand).getResultList();
    }
    public static List<Car> findCarsByModel(String model){
        return entityManager.createQuery("select c from Car c where c.model = ?0", Car.class)
                .setParameter(0, model).getResultList();
    }
    public static List<Car> findCarsByValue(Double value){
        return entityManager.createQuery("select c from Car c where c.value = ?0", Car.class)
                .setParameter(0, value).getResultList();
    }
    public static List<Car> findCarsByMileage(Integer mileage){
        return entityManager.createQuery("select c from Car c where c.mileage = ?0", Car.class)
                .setParameter(0, mileage).getResultList();
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

