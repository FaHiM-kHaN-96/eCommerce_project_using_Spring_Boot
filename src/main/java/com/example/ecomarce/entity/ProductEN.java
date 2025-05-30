package com.example.ecomarce.entity;


import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class ProductEN {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int product_id;
    private String product_name;
    private String product_description;
    private float buying_price;
    private float selling_price;
    private String product_category;
    @OneToMany(cascade=CascadeType.ALL)
    private List<OrderTableEN> order_tables;
    @OneToMany(cascade=CascadeType.ALL)
    private List<ImageEN> image = new ArrayList<>();
    @OneToMany(cascade=CascadeType.ALL)
    private List<Product_RatingEN> ratingENS = new ArrayList<>();
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private String templete_image;
    @OneToMany(cascade=CascadeType.ALL)
    private List<Common_UserEN> useren = new ArrayList<>();

    @Override
    public String toString() {
        return "ProductEN{" +
                "product_id=" + product_id +
                ", product_name='" + product_name + '\'' +
                ", product_description='" + product_description + '\'' +
                ", buying_price=" + buying_price +
                ", selling_price=" + selling_price +
                ", product_category='" + product_category + '\'' +
                ", order_tables=" + order_tables +
                ", image=" + image +
                ", ratingENS=" + ratingENS +
                ", templete_image='" + templete_image + '\'' +
                ", useren=" + useren +
                '}';
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public float getBuying_price() {
        return buying_price;
    }

    public void setBuying_price(float buying_price) {
        this.buying_price = buying_price;
    }

    public float getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(float selling_price) {
        this.selling_price = selling_price;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public List<OrderTableEN> getOrder_tables() {
        return order_tables;
    }

    public void setOrder_tables(List<OrderTableEN> order_tables) {
        this.order_tables = order_tables;
    }

    public List<ImageEN> getImage() {
        return image;
    }

    public void setImage(List<ImageEN> image) {
        this.image = image;
    }

    public List<Product_RatingEN> getRatingENS() {
        return ratingENS;
    }

    public void setRatingENS(List<Product_RatingEN> ratingENS) {
        this.ratingENS = ratingENS;
    }

    public String getTemplete_image() {
        return templete_image;
    }

    public void setTemplete_image(String templete_image) {
        this.templete_image = templete_image;
    }

    public List<Common_UserEN> getUseren() {
        return useren;
    }

    public void setUseren(List<Common_UserEN> useren) {
        this.useren = useren;
    }
}
