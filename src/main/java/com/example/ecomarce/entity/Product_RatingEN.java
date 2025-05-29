package com.example.ecomarce.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product_RatingEN {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private  String product_name;
    private int Product_rating;

    @Override
    public String toString() {
        return "Product_RatingEN{" +
                "id=" + id +
                ", product_name='" + product_name + '\'' +
                ", Product_rating=" + Product_rating +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_rating() {
        return Product_rating;
    }

    public void setProduct_rating(int product_rating) {
        Product_rating = product_rating;
    }

}
