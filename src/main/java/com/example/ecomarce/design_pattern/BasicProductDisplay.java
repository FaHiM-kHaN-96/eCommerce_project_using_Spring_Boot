package com.example.ecomarce.design_pattern;

import com.example.ecomarce.entity.ProductEN;

import java.util.List;

public class BasicProductDisplay implements ProductDisplayDecorator {

    @Override
    public String display(List<ProductEN> products) {
        StringBuilder result = new StringBuilder();
        for (ProductEN product : products) {
            result.append("Product: ")
                    .append(product.getProduct_name())
                    .append(", Description: ")
                    .append(product.getProduct_description())
                    .append(", Selling Price: ")
                    .append(product.getSelling_price())
                    .append(", Category: ")
                    .append(product.getProduct_category())
                    .append(", Images: ")
                    .append(product.getImage().size())
                    .append("\n");
        }
        return result.toString();
    }
}
