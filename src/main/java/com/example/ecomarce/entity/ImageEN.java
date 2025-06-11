package com.example.ecomarce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "product_img")
public class ImageEN {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String image;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private String imageData;

    @ManyToOne
    private ProductEN product_en;

    @Override
    public String toString() {
        return "ImageEN{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", imageData='" + imageData + '\'' +
                ", product_en=" + product_en +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }



    public ProductEN getProduct_en() {
        return product_en;
    }

    public void setProduct_en(ProductEN product_en) {
        this.product_en = product_en;
    }
}

