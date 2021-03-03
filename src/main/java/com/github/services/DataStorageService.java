package com.github.services;

import com.github.entities.Car;
import com.github.entities.Transaction;
import com.github.entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataStorageService implements DataStorageInterface{

    public static String PATH = "C:\\ProgramData\\CSM";
    public static final String TEMP_FLEET_PATH = PATH + "\\temp_fleet.csmdata";
    public static final String FLEET_PATH = PATH + "\\fleet.csmdata";
    public static final String TRANSACTIONS_PATH = PATH + "\\transactions.csmdata";
    public static final String TEMP_TRANSACTIONS_PATH = PATH + "\\temp_transactions.csmdata";
    public static final String AUTHENTICATION_PATH = PATH + "\\authentication.csmdata";

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence");
    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();





    public static List<User> loadUsers() throws IOException {

        return entityManager.createQuery("SELECT * FROM Users", User.class).getResultList();

    }
    public static void addUser(User user) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(AUTHENTICATION_PATH, true));

        String username = encryptData(user.getUsername());
        String passsword = encryptData(user.getPassword());
        String phone = encryptData(user.getPhone());

        writer.write(username);
        writer.write("; " + passsword);
        writer.write("; " + phone);
        writer.newLine();
        writer.close();

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

    public static ObservableList<Car> carsStoraged() throws IOException {

        verifyFiles();

        ArrayList<Car> fleet = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(FLEET_PATH));
        String line = reader.readLine();
        while (line != null && !line.equals("")){
            String[] dividedLine = line.split("; ");

            String brand = decryptData(dividedLine[0]);
            String model = decryptData(dividedLine[1]);
            Double value = Double.parseDouble(decryptData(dividedLine[2]));
            Integer mileage = Integer.parseInt(decryptData(dividedLine[3]));

            Car car = new Car(brand, model, value, mileage);
            fleet.add(car);

            line = reader.readLine();
        }

        reader.close();
        return FXCollections.observableArrayList(fleet);
    } // loads the cars from the file
    public static ObservableList<Transaction> transactions() throws IOException, ParseException {

        verifyFiles();

        ArrayList<Transaction> transactions = new ArrayList();

        BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_PATH));
        String line = reader.readLine();
        while (line != null && !line.equals("")){
            String[] dividedLine = line.split("; ");

            String brand = decryptData(dividedLine[0]);
            String model = decryptData(dividedLine[1]);
            Double value = Double.parseDouble(decryptData(dividedLine[2]));
            Integer mileage = Integer.parseInt(decryptData(dividedLine[3]));
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse((decryptData(dividedLine[4])));
            String salesman = decryptData(dividedLine[5]);

            Transaction transaction = new Transaction(new Car(brand, model, value, mileage), date, new User(salesman, null, null));
            transactions.add(transaction);

            line = reader.readLine();
        }

        reader.close();
        return FXCollections.observableArrayList(transactions);


    } //loads transactions from the file

    public static void addCarToFile(Car car) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter((FLEET_PATH), true));

        String brand = encryptData(car.getBrand());
        String model = encryptData(car.getModel());
        String value = encryptData(String.valueOf(car.getValue()));
        String mileage = encryptData(String.valueOf(car.getMileage()));

        writer.write(brand);
        writer.write("; " + model);
        writer.write("; " + value);
        writer.write("; " + mileage);
        writer.newLine();
        writer.close();

    }
    public static void removeCarFromFile(Car car) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(FLEET_PATH));

        String line = reader.readLine();
        while (line != null){ // finds the occurrence in the file

            String[] dividedLine = line.split("; ");

            if (decryptData(dividedLine[0]).equals(car.getBrand())
                    && decryptData(dividedLine[1]).equals(car.getModel())
                    && decryptData(dividedLine[2]).equals(String.valueOf(car.getValue()))
                    && decryptData(dividedLine[3]).equals(String.valueOf(car.getMileage()))){

                BufferedWriter writer = new BufferedWriter(new FileWriter(TEMP_FLEET_PATH));
                BufferedReader reader2 = new BufferedReader(new FileReader(FLEET_PATH));
                String currentLine = reader2.readLine();
                while(currentLine!= null) {

                    if (!currentLine.equals(line)) {
                        writer.write(currentLine);
                        writer.newLine();
                    }

                    currentLine = reader2.readLine();

                } // creates the new file (temp_fleet.csmdata)
                writer.close();
                reader2.close();


                try{ // replaces the old file for the new one (via cmd)
                    Process process = Runtime.getRuntime().exec(
                            "cmd /c " +
                                    "cd " + PATH + " " +
                                    "&& del fleet.csmdata " +
                                    "&& rename temp_fleet.csmdata fleet.csmdata"
                    );
                }
                catch (IOException e){
                    e.printStackTrace();
                }

            }
            line = reader.readLine();
        }

        reader.close();



    }

    public static void addTransaction(Transaction transaction) throws IOException, ParseException {

        BufferedWriter writer = new BufferedWriter(new FileWriter((TRANSACTIONS_PATH), true));

        String brand = encryptData(transaction.getCar().getBrand());
        String model = encryptData(transaction.getCar().getModel());
        String value = encryptData(String.valueOf(transaction.getCar().getValue()));
        String mileage = encryptData(Integer.toString(transaction.getCar().getMileage()));
        String date = encryptData(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(transaction.getDate())) ;
        String salesman = encryptData(transaction.getSalesman().getUsername());

        writer.write(brand);
        writer.write("; " + model);
        writer.write("; " + value);
        writer.write("; " + mileage);
        writer.write("; " + date);
        writer.write("; " + salesman);
        writer.newLine();
        writer.close();

    }
    public static void removeTransactionFromFile(Transaction transaction) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_PATH));

        String line = reader.readLine();
        while (line != null){ // finds the occurrence in the file

            String[] dividedLine = line.split("; ");

            if (decryptData(dividedLine[0]).equals(transaction.getCar().getBrand())
                    && decryptData(dividedLine[1]).equals(transaction.getCar().getModel())
                    && decryptData(dividedLine[2]).equals(String.valueOf(transaction.getCar().getValue()))
                    && decryptData(dividedLine[3]).equals(String.valueOf(transaction.getCar().getMileage()))
                    && decryptData(dividedLine[4]).equals(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(transaction.getDate()))
                    && decryptData(dividedLine[5]).equals(transaction.getSalesman().getUsername())) {

                BufferedWriter writer = new BufferedWriter(new FileWriter(TEMP_TRANSACTIONS_PATH));
                BufferedReader reader2 = new BufferedReader(new FileReader(TRANSACTIONS_PATH));
                String currentLine = reader2.readLine();
                while(currentLine!= null) {

                    if (!currentLine.equals(line)) {
                        writer.write(currentLine);
                        writer.newLine();
                    }

                    currentLine = reader2.readLine();

                } // creates the new file (temp_transactions.csmdata)
                writer.close();
                reader2.close();


                try{
                    Process process = Runtime.getRuntime().exec(
                            "cmd /c " +
                                    "cd " + PATH + " " +
                                    "&& del transactions.csmdata " +
                                    "&& rename temp_transactions.csmdata transactions.csmdata"
                    );
                } // replaces the old file for the new one (via cmd)
                catch (IOException e){
                    e.printStackTrace();
                }

            }
            line = reader.readLine();
        }

        reader.close();

    }


    private static void verifyFiles() {
        if(entityManager.createQuery("SHOW TABLES LIKE 'User").getResultList().size() == 0){
            entityManager.getTransaction().begin();
            entityManager.createQuery("CREATE TABLE User");
            entityManager.getTransaction().commit();
        }
        else if(entityManager.createQuery("SHOW TABLES LIKE 'Car").getResultList().size() == 0){
            entityManager.getTransaction().begin();
            entityManager.createQuery("CREATE TABLE Car");
            entityManager.getTransaction().commit();
        }
        else if(entityManager.createQuery("SHOW TABLES LIKE 'Transaction").getResultList().size() == 0){
            entityManager.getTransaction().begin();
            entityManager.createQuery("CREATE TABLE Transaction");
            entityManager.getTransaction().commit();
        }
    }
    public static Boolean verifyUserData(User user) throws IOException {
        for (User u : DataStorageService.loadUsers()){
            System.out.println(u);
            if (u.getUsername().equals(user.getUsername()) &&
                    u.getPassword().equals(user.getPassword())){
                return true;
            }
        }
        return false;
    }


}

