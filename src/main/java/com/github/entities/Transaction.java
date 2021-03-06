package com.github.entities;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;



@Entity
@Access(AccessType.FIELD)
@Table(name = "transactions")
public class Transaction implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String carBrand;
    private String carModel;
    private Double carValue;
    private Integer carMileage;
    @Temporal(TemporalType.DATE)
    private Date date;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User salesman;

    public Transaction(Integer id, Car car, Date date, User salesman) {
        this.id = id;
        this.carBrand = car.getBrand();
        this.carModel = car.getModel();
        this.carValue = car.getValue();
        this.carMileage = car.getMileage();
        this.date = date;
        this.salesman = salesman;
    }

    public Transaction() {
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarBrand() {
        return carBrand;
    }
    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }
    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public Integer getCarMileage() {
        return carMileage;
    }
    public void setCarMileage(Integer carMileage) {
        this.carMileage = carMileage;
    }

    public Double getCarValue() {
        return carValue;
    }
    public void setCarValue(Double value) {
        this.carValue = value;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public User getSalesman() {
        return salesman;
    }
    public void setSalesman(User salesman) {
        this.salesman = salesman;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", carBrand='" + carBrand + '\'' +
                ", carModel='" + carModel + '\'' +
                ", carValue=" + carValue +
                ", carMileage=" + carMileage +
                ", date=" + date +
                ", salesman=" + salesman +
                '}';
    }
}
