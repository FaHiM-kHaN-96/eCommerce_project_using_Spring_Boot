package com.example.ecomarce.service_pkg;

import com.example.ecomarce.repo.Order_Manage;
import com.example.ecomarce.repo.ProDuct_repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Product_Service {
    @Autowired
    private ProDuct_repo product_repo;
    @Transactional
    public boolean Update_payment_Status(int product_id,double rating) {

        return product_repo.set_rating(product_id, rating) ;
    }
}
