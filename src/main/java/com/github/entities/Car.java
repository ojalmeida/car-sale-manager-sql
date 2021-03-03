package com.github.entities;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "Cars")
public class Car implements Serializable {


    private Integer id;
    private final SimpleStringProperty brand;
    private final SimpleStringProperty model;
    private final SimpleDoubleProperty value;
    private final SimpleIntegerProperty mileage;

    public Car(String brand, String model, Double value, Integer mileage) {
        this.brand = new SimpleStringProperty(brand);
        this.model = new SimpleStringProperty(model);
        this.value = new SimpleDoubleProperty(value);
        this.mileage = new SimpleIntegerProperty(mileage);
    }

    @Column(name = "Id")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "Brand")
    public String getBrand() {
        return brand.get();
    }
    public void setBrand(String brand) {
        this.brand.set(brand);
    }

    @Column(name = "Model")
    public String getModel() {
        return model.get();
    }
    public void setModel(String model) {
        this.model.set(model);
    }

    @Column(name = "Value")
    public double getValue() {
        return value.get();
    }
    public void setValue(double value) {
        this.value.set(value);
    }

    @Column(name = "Mileage")
    public int getMileage() {
        return mileage.get();
    }
    public void setMileage(int mileage) {
        this.mileage.set(mileage);
    }


    public SimpleStringProperty brandProperty() {
        return brand;
    }
    public SimpleStringProperty modelProperty() {
        return model;
    }
    public SimpleDoubleProperty valueProperty() {
        return value;
    }
    public SimpleIntegerProperty mileageProperty() {
        return mileage;
    }


    @Override
    public String toString() {
        return "[ " + getBrand() + ", " + getModel() + ", " + getValue() + ", " + getMileage() + " ]";
    }
}
