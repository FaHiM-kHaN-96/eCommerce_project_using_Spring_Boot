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
    private float order_total_price;
    private String order_payment_method;
    private String order_status;
    private String order_date;
    private String invoice_id;
    @ManyToOne
    private Common_UserEN user;

}
