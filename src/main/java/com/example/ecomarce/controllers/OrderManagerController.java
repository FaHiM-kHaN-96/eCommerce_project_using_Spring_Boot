package com.example.ecomarce.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderManagerController {


   @GetMapping("/page")
    public String orderpage(){


        return "adminpg/order_manager";
    }

}
