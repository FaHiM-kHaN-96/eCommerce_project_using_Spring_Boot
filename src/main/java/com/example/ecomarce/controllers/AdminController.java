package com.example.ecomarce.controllers;

import com.example.ecomarce.entity.Common_UserEN;
import com.example.ecomarce.entity.ImageEN;
import com.example.ecomarce.entity.OrderTableEN;
import com.example.ecomarce.entity.ProductEN;

import com.example.ecomarce.helper.MessAge;

import com.example.ecomarce.repo.Order_Manage;
import com.example.ecomarce.repo.ProDuct_repo;
import com.example.ecomarce.repo.adminrepo.Change_role_repo;
import com.example.ecomarce.service_pkg.adminservice.RoleChangerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private RoleChangerService roleChangerService;
    @Autowired
    private Change_role_repo change_role_repo    ;
    @Autowired
    private ProDuct_repo proDuct_repo;

    @Autowired
    private Order_Manage order_manage;




    @RequestMapping(value = "/admin_input" , method = RequestMethod.POST)
    public String admin_page_Controller (@RequestParam("mail") String mail,
                                         @RequestParam("role") String role,
                                         Model model, HttpSession session) {


        System.out.println("Admin addition data  " +mail  +"   " + role);
        if (role.equals("order")) {


            if (roleChangerService.ChangeRole(mail, "ROLE_ORDER")){
                System.out.println("Role Order Manager");
                session.setAttribute("message",new MessAge("Order manager Addition Success","success-message") );
               // return "redirect:/admin/lg";
            }else {
                System.out.println("Role Fail ");
                session.setAttribute("message",new MessAge("Order manager Addition Failed","error-message") );

            }


        } else if (role.equals("product")) {

            System.out.println("Role Product Manager");
            if ( roleChangerService.ChangeRole(mail, "ROLE_PRODUCT")){
                session.setAttribute("message",new MessAge("Product Manager Addition Success","success-message") );
            }else {
                System.out.println("Role Fail 2");
                session.setAttribute("message",new MessAge("Product Manager Addition Failed","error-message") );
            }

        }

        return "redirect:/admin/lg";
    }

    @PostMapping("/add")
    public String products_add(@ModelAttribute("product_details") ProductEN productEN,
                               @RequestParam("images") MultipartFile[] images,
                               HttpSession session) {
        try {
            // Validate input
            if (productEN == null) {
                throw new IllegalArgumentException("Product details cannot be null");
            }


            if (productEN.getImage() == null) {
                productEN.setImage(new ArrayList<>());
            }

            for (MultipartFile image : images) {
                if (!image.isEmpty()) {

                    byte[] bytes = image.getBytes();
                    String base64 = Base64.getEncoder().encodeToString(bytes);
                    productEN.setTemplete_image(base64);
                    ImageEN imageEN = new ImageEN();
                    imageEN.setImageData(base64);
                    imageEN.setProduct_en(productEN);
                    productEN.getImage().add(imageEN);
                }

            }


            proDuct_repo.save(productEN);
            session.setAttribute("message_p", new MessAge("Product Added Successfully", "success-message"));

        } catch (Exception e) {
            session.setAttribute("message_p", new MessAge("Error adding product: " + e.getMessage(), "error-message"));
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("product_details", new ProductEN());
        return "adminpg/admin_products";

    }

    @GetMapping("/lg")
    public String admin(Model model) {


        List<Common_UserEN> user = change_role_repo.findAll();
        List<Common_UserEN> userdatamap = new ArrayList();
        List<OrderTableEN> pendinglist = order_manage.findByStatus("pending");

        for (Common_UserEN link : user) {
            try {
                if (!link.getRole().equals("ROLE_USER")) {
                    userdatamap.add(link);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("order_list" , pendinglist);
        model.addAttribute("userdatamap", userdatamap);
        return "adminpg/admin";

    }

}
