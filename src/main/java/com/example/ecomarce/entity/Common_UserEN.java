package com.example.ecomarce.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "common_user")
public class Common_UserEN {

@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;
@Column(unique = true, nullable = false)
private String username;
private String password;
private String fullname;
private String address;
private String phone;
private String role;
private boolean is_verified;
private String device_one;
private String device_two;
private String device_ip_one;
private String device_ip_two;

    public String getDevice_one() {
        return device_one;
    }

    public void setDevice_one(String device_one) {
        this.device_one = device_one;
    }

    public String getDevice_two() {
        return device_two;
    }

    public void setDevice_two(String device_two) {
        this.device_two = device_two;
    }

    public String getDevice_ip_one() {
        return device_ip_one;
    }

    public void setDevice_ip_one(String device_ip_one) {
        this.device_ip_one = device_ip_one;
    }

    public String getDevice_ip_two() {
        return device_ip_two;
    }

    public void setDevice_ip_two(String device_ip_two) {
        this.device_ip_two = device_ip_two;
    }

    @OneToMany(cascade=CascadeType.ALL)
private List<OrderTableEN> order_tables = new ArrayList<>();

@ManyToOne
private ProductEN producten;

    @Override
    public String toString() {
        return "Common_UserEN{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fullname='" + fullname + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", is_verified=" + is_verified +
                ", device_one='" + device_one + '\'' +
                ", device_two='" + device_two + '\'' +
                ", device_ip_one='" + device_ip_one + '\'' +
                ", device_ip_two='" + device_ip_two + '\'' +
                ", order_tables=" + order_tables +
                ", producten=" + producten +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isIs_verified() {
        return is_verified;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }

    public List<OrderTableEN> getOrder_tables() {
        return order_tables;
    }

    public void setOrder_tables(List<OrderTableEN> order_tables) {
        this.order_tables = order_tables;
    }



    public ProductEN getProducten() {
        return producten;
    }

    public void setProducten(ProductEN producten) {
        this.producten = producten;
    }
}
