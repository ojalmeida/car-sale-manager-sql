package com.github.entities;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;



@Entity
@Access(AccessType.PROPERTY)
@Table(name = "Transactions")
public class Transaction implements Serializable {


    private Integer id;
    private final ObjectProperty<Car> carProperty;
    private final ObjectProperty<Date> dateProperty;
    private final ObjectProperty<User> salesmanProperty;


    public Transaction(Car car, Date date, User salesman) {
        carProperty = new SimpleObjectProperty<>();
        carProperty.set(car);
        dateProperty = new SimpleObjectProperty<>();
        dateProperty.set(date);
        salesmanProperty = new SimpleObjectProperty<>();
        salesmanProperty.set(salesman);


    }


    /// To map
    @Column(name = "Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "Car")
    public Integer getCarId(){
        return carProperty.get().getId();
    }
    public void setCarId(Integer id){this.carProperty.get().setId(id);}

    @Column(name = "Date")
    public Date getDate() {
        return dateProperty.get();
    }
    public void setDate(Date date){
        this.dateProperty.set(date);
    }

    @Column(name = "Salesman")
    public Integer getSalesmanId(){
        return salesmanProperty.get().getId();
    }

    public void setSalesmanId(Integer id){this.salesmanProperty.get().setId(id);}

    ///Transients

    @Transient
    public Car getCar() {
        return carProperty.get();
    }
    @Transient
    public User getSalesman() {
        return salesmanProperty.get();
    }
    @Transient
    public ObjectProperty<Car> getCarProperty() {
        return carProperty;
    }
    @Transient
    public ObjectProperty<Date> getDateProperty() {
        return dateProperty;
    }
    @Transient
    public ObjectProperty<User> getSalesmanProperty() {
        return salesmanProperty;
    }

}
