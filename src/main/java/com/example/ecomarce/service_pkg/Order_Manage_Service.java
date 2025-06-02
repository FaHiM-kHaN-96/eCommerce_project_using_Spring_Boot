package com.example.ecomarce.service_pkg;

import com.example.ecomarce.repo.Check_Verifcation;
import com.example.ecomarce.repo.Order_Manage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Order_Manage_Service {
    @Autowired
    private Order_Manage order_manage;

    public boolean InvoiceID_checker(String inv) {
        Boolean exists = order_manage.findinvoiceId(inv);
        System.out.println("Value of exists: " + exists);
        return exists != null && exists;
    }

    @Transactional
    public boolean Update_Order_Status(String invoice_id,String stetus ,String payment_status  ,String payment_method ) {

        return order_manage.updateOrderInfo(invoice_id,stetus,payment_status,payment_method) ;
    }
    @Transactional
    public boolean Update_payment_Status(String invoice_id,String stetus) {

        return order_manage.get_invoice_id_p(invoice_id,stetus) ;
    }

    @Transactional
    public boolean Update_order_rating(int order_id,int rating) {

        return order_manage.updateOrder_rate(order_id,rating) ;
    }
}
