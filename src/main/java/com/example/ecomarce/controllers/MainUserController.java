package com.example.ecomarce.controllers;

import com.example.ecomarce.entity.Common_UserEN;
import com.example.ecomarce.entity.OrderTableEN;
import com.example.ecomarce.entity.ProductEN;
import com.example.ecomarce.generic_logic.PinGenerator;
import com.example.ecomarce.helper.MessAge;
import com.example.ecomarce.logical_class.Cart_Add;
import com.example.ecomarce.repo.Order_Manage;
import com.example.ecomarce.repo.ProDuct_repo;
import com.example.ecomarce.repo.UserAuth;
import com.example.ecomarce.service_pkg.EmailService;
import com.example.ecomarce.service_pkg.Order_Manage_Service;
import com.example.ecomarce.service_pkg.Verifcation_Ser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    private Order_Manage_Service orderManageService;
    @Autowired
    private Order_Manage orderManage;


    private PinGenerator pinGenerator ;





  private final List<Cart_Add> cartlist = new ArrayList<>();

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

        cart_add.setProduct_id(productEN.getProduct_id());
        cart_add.setQuantity(quantity);
        cart_add.setSubtotal(productEN.getSelling_price()*quantity);
        System.out.println("\n\nSUbtotal  list "+productEN.getSelling_price()*quantity);
        cart_add.setProduct_name(productEN.getProduct_name());
        cart_add.setSelling_price(productEN.getSelling_price());
        cart_add.setProduct_category(productEN.getProduct_category());
        cart_add.setProduct_image(productEN.getTemplete_image());

        cartlist.add(cart_add);
        //cart_add= null;
        System.out.println(cartlist);
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


    @PostMapping("/checkout_cm")
    public String checkout_cm(
                              //@RequestParam("address") String address ,
                              @RequestParam("transaction_id") String transaction_id,
                              Model model, HttpSession session, Principal principal) {
        //System.out.println("\n\n\n\nFullname "+fullname);
       // System.out.println("\n\nphone "+phone);
      //  System.out.println("\n\naddress "+address);
        System.out.println("\n\npayment_method "+transaction_id);
        System.out.println("\n\n\n\npin   "+invoice_checker(PinGenerator.generateSixDigitPin()));
        String transaction_data = transaction_id;
        //session.setAttribute("transaction_data    ", transaction_data);
        LocalTime time = LocalTime.now();

        // 12-hour clock formatter with AM/PM
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        // Format time
        String formattedTime = time.format(formatter);
        String invoice = invoice_checker(invoice_checker(PinGenerator.generateSixDigitPin()));
        System.out.println("time  "+ formattedTime);
        try {
            String invoice_data = null;
            for (Cart_Add item : cartlist) {
                invoice_data= invoice;
                OrderTableEN orderTableEN = new OrderTableEN();
                System.out.println("\n\n\nproducts list "+item.getProduct_name());
                orderTableEN.setOrder_product_id(item.getProduct_id());
                orderTableEN.setOrder_product_name(item.getProduct_name());
                orderTableEN.setOrder_subtotal(item.getSubtotal());
                System.out.println("\n\n\norder table obj subtotal "+item.getSubtotal());
                orderTableEN.setOrder_selling_price(item.getSelling_price());
                orderTableEN.setOrder_quantity(item.getQuantity());
                //orderTableEN.setOrder_total_price(calculateTotalPrice());
                orderTableEN.setOrder_product_category(item.getProduct_category());
                orderTableEN.setOrder_payment_method(transaction_data);
                if (transaction_data.equals("Cash on Delivery")) {
                    orderTableEN.setOrder_payment_status("Cash on Delivery");
                }
                orderTableEN.setOrder_payment_status("Pending");
                orderTableEN.setOrder_status("Pending");
                orderTableEN.setOrder_date(formattedTime);
                orderTableEN.setProducten(proDuct_repo.findByProduct_id(item.getProduct_id()));
                orderTableEN.setUser(userAuth.findByUsername(principal.getName()));
                orderTableEN.setInvoice_id(invoice_data);
                orderManage.save(orderTableEN);

            }
            cartlist.clear();
            session.setAttribute("message_check",new MessAge("Order Processed Successfully","success-message") );
            return "userpg/order_status";
        } catch (Exception e) {

            session.setAttribute("message_check",new MessAge("Order Failed"+e.getMessage(),"error-message") );

        }
// All courted all product should be uploaded on database with single inv number
        return "userpg/order_status";

    }
    private String invoice_checker(String invoice_id) {

        String invoice =invoice_id;

        if (orderManageService.InvoiceID_checker(invoice)) {
            return invoice_checker(invoice_checker(PinGenerator.generateSixDigitPin()));

        }
        invoice = invoice_id;

        return invoice;
    }
    @GetMapping("/checkout" )
    public String checkout(Principal principal, Model model) {
        System.out.println(principal.getName());
        Common_UserEN commonUserEN =  userAuth.findByUsername(principal.getName());
        System.out.println(commonUserEN);
        System.out.println("All proce");

        model.addAttribute("order_product",cartlist);
        model.addAttribute("total_price",calculateTotalPrice());
        model.addAttribute("commonUserEN", commonUserEN);

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
