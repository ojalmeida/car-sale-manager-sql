package com.github.entities;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;



@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Car car;
    private Date date;
    private User salesman;

    public Transaction(Integer id, Car car, Date date, User salesman) {
        this.id = id;
        this.car = car;
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

    public Car getCar() {
        return car;
    }
    public void setCar(Car car) {
        this.car = car;
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
                ", car=" + car +
                ", date=" + date +
                ", salesman=" + salesman +
                '}';
    }
}
