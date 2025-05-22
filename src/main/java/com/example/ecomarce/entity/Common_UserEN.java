package com.example.ecomarce.entity;

import jakarta.persistence.*;

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
@OneToMany(cascade=CascadeType.ALL)
private List<OrderTableEN> order_tables;

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
                ", order_tables=" + order_tables +
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
}
