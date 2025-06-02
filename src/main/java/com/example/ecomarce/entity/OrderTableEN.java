package com.example.ecomarce.entity;

import jakarta.persistence.*;

@Entity
public class OrderTableEN {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int order_id;
    private int order_product_id;
    private String order_product_name;
    private float order_subtotal;
    private int order_quantity;
    private float order_selling_price;
    private String order_product_category;
    private int rating;
    private String order_payment_method;
    private String order_status;
    private String order_date;
    private String invoice_id;
    private String order_payment_status;
    private float order_payment_amount;
    @ManyToOne
    private Common_UserEN user;

    @Override
    public String toString() {
        return "OrderTableEN{" +
                "order_id=" + order_id +
                ", order_product_id=" + order_product_id +
                ", order_product_name='" + order_product_name + '\'' +
                ", order_subtotal=" + order_subtotal +
                ", order_quantity=" + order_quantity +
                ", order_selling_price=" + order_selling_price +
                ", order_product_category='" + order_product_category + '\'' +
                ", rating=" + rating +
                ", order_payment_method='" + order_payment_method + '\'' +
                ", order_status='" + order_status + '\'' +
                ", order_date='" + order_date + '\'' +
                ", invoice_id='" + invoice_id + '\'' +
                ", order_payment_status='" + order_payment_status + '\'' +
                ", order_payment_amount=" + order_payment_amount +
                ", user=" + user +
                ", producten=" + producten +
                '}';
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getOrder_product_id() {
        return order_product_id;
    }

    public void setOrder_product_id(int order_product_id) {
        this.order_product_id = order_product_id;
    }

    public String getOrder_product_name() {
        return order_product_name;
    }

    public void setOrder_product_name(String order_product_name) {
        this.order_product_name = order_product_name;
    }

    public float getOrder_subtotal() {
        return order_subtotal;
    }

    public void setOrder_subtotal(float order_subtotal) {
        this.order_subtotal = order_subtotal;
    }

    public int getOrder_quantity() {
        return order_quantity;
    }

    public void setOrder_quantity(int order_quantity) {
        this.order_quantity = order_quantity;
    }

    public float getOrder_selling_price() {
        return order_selling_price;
    }

    public void setOrder_selling_price(float order_selling_price) {
        this.order_selling_price = order_selling_price;
    }

    public String getOrder_product_category() {
        return order_product_category;
    }

    public void setOrder_product_category(String order_product_category) {
        this.order_product_category = order_product_category;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getOrder_payment_method() {
        return order_payment_method;
    }

    public void setOrder_payment_method(String order_payment_method) {
        this.order_payment_method = order_payment_method;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getOrder_payment_status() {
        return order_payment_status;
    }

    public void setOrder_payment_status(String order_payment_status) {
        this.order_payment_status = order_payment_status;
    }

    public float getOrder_payment_amount() {
        return order_payment_amount;
    }

    public void setOrder_payment_amount(float order_payment_amount) {
        this.order_payment_amount = order_payment_amount;
    }

    public Common_UserEN getUser() {
        return user;
    }

    public void setUser(Common_UserEN user) {
        this.user = user;
    }

    public ProductEN getProducten() {
        return producten;
    }

    public void setProducten(ProductEN producten) {
        this.producten = producten;
    }

    @ManyToOne
    private ProductEN producten;



}
