package com.example.ecomarce.service_pkg;

import com.example.ecomarce.repo.Check_Verifcation;
import com.example.ecomarce.repo.Order_Manage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Order_Manage_Service {
    @Autowired
    private Order_Manage order_manage;

    public boolean InvoiceID_checker(String inv) {
        Boolean exists = order_manage.findinvoiceId(inv);
        System.out.println("Value of exists: " + exists);
        return exists != null && exists;
    }
}
