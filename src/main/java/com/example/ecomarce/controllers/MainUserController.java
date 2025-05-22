package com.example.ecomarce.controllers;

import com.example.ecomarce.entity.Common_UserEN;
import com.example.ecomarce.entity.ProductEN;
import com.example.ecomarce.helper.MessAge;
import com.example.ecomarce.logical_class.Cart_Add;
import com.example.ecomarce.repo.Image_Repo;
import com.example.ecomarce.repo.ProDuct_repo;
import com.example.ecomarce.repo.UserAuth;
import com.example.ecomarce.service_pkg.EmailService;
import com.example.ecomarce.service_pkg.Verifcation_Ser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller

public class MainUserController {
    @Autowired
    private UserAuth userAuth;
    @Autowired
    private Verifcation_Ser checkVerifcation;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ProDuct_repo proDuct_repo;
    @Autowired
    private Image_Repo imagelist;




  private List<Cart_Add> cartlist = new ArrayList<>();

    @GetMapping("/login")
    public String login() {
        return "userpg/login";
    }


    @GetMapping("/")
    public String product( Model model) {
       // String email = principal.getName();
        List<ProductEN> productlist = proDuct_repo.findAll();


        int count = cartlist.size();
        String number = String.valueOf(count);

        System.out.println("All cart  "+ number);
        
        model.addAttribute("cart_dt", number);
        model.addAttribute("imagelst", productlist);
        model.addAttribute("productlist", productlist);
        model.addAttribute("dtl", new Common_UserEN());
        model.addAttribute("list_cart", new ProductEN());
        return "userpg/index";

    }
    @PostMapping("/add/{id}")
    public String add_cart_product(@PathVariable("id") int id,
                                   @RequestParam("quantity") int quantity,Model model) {
        ProductEN productEN = proDuct_repo.findByProduct_id(id);
        Cart_Add cart_add = new Cart_Add();

        cart_add.setProduct_id(id);
        cart_add.setQuantity(quantity);
        cart_add.setSubtotal(productEN.getSelling_price()*quantity);
        cart_add.setProduct_name(productEN.getProduct_name());
        cart_add.setSelling_price(productEN.getSelling_price());
        cart_add.setProduct_category(productEN.getProduct_category());
        cart_add.setProduct_image(productEN.getTemplete_image());



        cartlist.add(cart_add);
        return "redirect:/";
    }
    private float calculateTotalPrice() {
        float total = 0;
        for (Cart_Add item : cartlist) {
            total += item.getSubtotal();
        }
        return total;
    }
    @PostMapping("/cart")
    public String cart_product(Model model) {



        model.addAttribute("total", calculateTotalPrice());
        model.addAttribute("list_cart",cartlist );


        return "userpg/cart";
    }

    private void emaiilSender(String email) {
        String link = "http://localhost:8080/verification/"+email;
        emailService.sendVerificationEmail(email,link);
    }

    @PostMapping("/details/{id}")
    public String details_hd (@PathVariable("id") int id, Model model) {

        ProductEN productlist = proDuct_repo.findByProduct_id(id);

        Map<Integer, ProductEN> product = new HashMap<>();

        try {

            product.put(productlist.getProduct_id(), productlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("product", product);
        return "userpg/product_details";
    }


    @PostMapping("/checkout")
    public String checkout() {
        return "userpg/checkout";
    }


    @GetMapping("/verify")
    public String verify() {
        return "userpg/verify";
    }
    @GetMapping("/verification/{email}")
    public String verification(@PathVariable("email") String email) {
        System.out.println("\n\n\nMail cheker  "+email);
//        checkVerifcation.verify(email,true);
        if (checkVerifcation.verify(email,true)) {
            return "redirect:/";
        }
        return "redirect:/verify";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("customer", new Common_UserEN());
        return "userpg/signup";

    }

    @RequestMapping(value = "/signup_successful",method = RequestMethod.POST)
    public String signupsub(Model model,
                            @ModelAttribute("customer") Common_UserEN customer,
                            HttpSession session) {

        try {
            customer.setPassword(new BCryptPasswordEncoder(12).encode(customer.getPassword()));
            customer.setRole("ROLE_USER");
            userAuth.save(customer);
            session.setAttribute("message",new MessAge("Signup Successful","success-message") );
        } catch (Exception e) {

            session.setAttribute("message",new MessAge("Signup Failed"+e.getMessage(),"error-message") );

        }

        return "redirect:/signup";
    }



}
