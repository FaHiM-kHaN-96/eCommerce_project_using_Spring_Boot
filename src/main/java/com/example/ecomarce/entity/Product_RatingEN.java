package com.example.ecomarce.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "rating_table")
public class Product_RatingEN {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int rid;
    private  String product_name;
    private int Product_rating;
    private String invoice_id;
    private int product_id;
    @ManyToOne
    private ProductEN productRatingEN;

    @Override
    public String toString() {
        return "Product_RatingEN{" +
                "rid=" + rid +
                ", product_name='" + product_name + '\'' +
                ", Product_rating=" + Product_rating +
                ", invoice_id='" + invoice_id + '\'' +
                ", product_id=" + product_id +
                ", productRatingEN=" + productRatingEN +
                '}';
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
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

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public ProductEN getProductRatingEN() {
        return productRatingEN;
    }

    public void setProductRatingEN(ProductEN productRatingEN) {
        this.productRatingEN = productRatingEN;
    }
}
