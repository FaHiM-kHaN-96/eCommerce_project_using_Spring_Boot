package com.example.ecomarce.design_pattern;

import com.example.ecomarce.entity.ProductEN;

import java.util.List;

public interface ProductDisplayDecorator {
    String display(List<ProductEN> products);
}
