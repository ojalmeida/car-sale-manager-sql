package com.github.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Users")
public class User implements Serializable {

    @Column(name = "Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "Username")
    private String username;
    @Column(name = "Password")
    private String password;
    @Column(name = "Phone")
    private String phone;

    public User(String username, String password, String phone) {
    this.username = username;
    this.password = password;
    this.phone = phone;
    }
    public User(String username, String password) {
    this.username = username;
    this.password = password;
    }

    public User() {

    }

    public Integer getId() {
        return id;
    }
    public String getUsername() {

        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getPhone() {
        return phone;
    }


    public void setId(Integer id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public String toString() {
        return "User{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", phone='" + phone + '\'' +
            '}';
    }
}
