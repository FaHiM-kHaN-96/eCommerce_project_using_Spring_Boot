package com.example.ecomarce.controllers;

import com.example.ecomarce.entity.Common_UserEN;
import com.example.ecomarce.entity.OrderTableEN;
import com.example.ecomarce.entity.ProductEN;
import com.example.ecomarce.generic_logic.PinGenerator;
import com.example.ecomarce.helper.MessAge;
import com.example.ecomarce.logical_class.Cart_Add;
import com.example.ecomarce.pdf_maker_class.CustomerInvoice;
import com.example.ecomarce.repo.Order_Manage;
import com.example.ecomarce.repo.ProDuct_repo;
import com.example.ecomarce.repo.UserAuth;
import com.example.ecomarce.service_pkg.EmailService;
import com.example.ecomarce.service_pkg.Order_Manage_Service;
import com.example.ecomarce.service_pkg.Verifcation_Ser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
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



    private final List<Cart_Add> cartlist = new ArrayList<>();

    @GetMapping("/login")
    public String login() {
        return "userpg/login";
    }


    @GetMapping("/")
    public String product( Model model) {
        // String email = principal.getName();
        List<ProductEN> productlist = proDuct_repo.findAll();

        double averageRating = 0.0;
        for (ProductEN product : productlist) {

            int total_product =0;
            double ratesum = 0.0;
            // double result = 0.0;
            List<OrderTableEN> orderTableENS = orderManage.findallproduct(product.getProduct_id());
            for (OrderTableEN ordercount : orderTableENS) {
                int rating = ordercount.getRating();
                ratesum += rating;
                total_product ++;

            }
            double rating = ratesum / total_product;
            System.out.println("AVG rating "+product.getProduct_avg_rating());
            if (rating > 0) {
                proDuct_repo.set_rating(product.getProduct_id(), ratesum / total_product);

            }
            total_product =0;
            ratesum = 0.0;
            System.out.println("rating check  "+product.getProduct_id() +"  "+ ratesum/total_product);




        }


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


    @GetMapping("/delete-c-p/{id}")
    public String delete_cart_product(@PathVariable("id") int id) {
        for (int i = 0; i < cartlist.size(); i++) {
            if (cartlist.get(i).getProduct_id() == id) {
                cartlist.remove(i);
                break; // Exit loop after removal
            }
        }
        return "redirect:/cart";
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











    @PostMapping("/product_rating/{id}/rate/{pid}/{oid}")
    public String getOrderProducts(@RequestParam("rate_prd") int rate,@PathVariable("id") String id,@PathVariable("pid") int pid,@PathVariable("oid") int oid,
                                   Principal principal,
                                   Model model) {

        System.out.println("\n\nProduct Rating  "+rate+"   "+principal.getName()+" "+oid);

        // System.out.println("    "+orderTableEN.getOrder_product_name());

        try {


            if (orderManageService.Update_order_rating(oid,rate)){

                System.out.println("Product Rated successfully  ");
            }else {

                System.out.println("Product Rated failed ");
            }


//            productRatingEN.setProduct_id(pid);
//            productRatingEN.setProduct_name(orderTableEN.getOrder_product_name());
//            productRatingEN.setInvoice_id(id);
//            productRatingEN.setProductRatingEN(orderTableEN.getProducten());
//            productRatingEN.setCommon_UserEN(userAuth.findByUsername(principal.getName()));
//            reting_Repo.save(productRatingEN);
           // productRatingEN.setProduct_name();
        }catch (Exception e) {

            e.printStackTrace();
        }

        return "redirect:/order-list" ;
    }


















    @GetMapping("/cart")
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
            @RequestParam(value = "transaction_id", required = false) String transaction_id,
            Model model, HttpSession session, Principal principal) {

        
        String transaction_data = transaction_id;

        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy, hh:mm a");

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

                if (transaction_data == null) {

                    orderTableEN.setOrder_payment_method("Cash On Delivery");
                } else {
                    orderTableEN.setOrder_payment_method(transaction_data+" (Online)");

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
    public String checkout(Principal principal, Model model,HttpSession session) {
        System.out.println(principal.getName());
        Common_UserEN commonUserEN =  userAuth.findByUsername(principal.getName());
        System.out.println(commonUserEN);
        System.out.println("All proce");
        if (cartlist.isEmpty()) {

            session.setAttribute("no_product_available",new MessAge("No Product available on cart to process ","warning-message") );
            return "redirect:/cart";
        }
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

    @GetMapping("/order-list")
    public String order_list(Model model,Principal principal) {



        Common_UserEN commonUserEN =  userAuth.findByUsername(principal.getName());

        Map<OrderTableEN,List<OrderTableEN>> ord = new HashMap<>();

        List<OrderTableEN> orderlist = orderManage.findOrderList(commonUserEN.getId());
        Map<String,Float> total_price = new HashMap<>();
        Set<String> seenInvoiceIds = new HashSet<>();


        for (OrderTableEN order : orderlist) {
            List<OrderTableEN> same_invoice_order = orderManage.findinvoice(order.getInvoice_id());


            float total_price_sum = 0 ;



            if(!seenInvoiceIds.contains(order.getInvoice_id())) {
                List<OrderTableEN> same_invoice_order2 = orderManage.findinvoice(order.getInvoice_id());
                for (OrderTableEN order2 : same_invoice_order2) {
                    System.out.println(order2.getInvoice_id());
                    total_price_sum += order2.getOrder_subtotal();
                }

                if (order.getOrder_payment_amount() == null || order.getOrder_payment_amount() == 0) {
                    orderManage.set_order_price(order.getInvoice_id(),total_price_sum);
                    System.out.println("All set");
                }else {
                    System.out.println("All set previously");
                }

                System.out.println("\n\n\norder list " + total_price_sum);
                total_price_sum =0;
                seenInvoiceIds.add(same_invoice_order.get(0).getInvoice_id());
                ord.put(order,same_invoice_order);

            }

        }

        model.addAttribute("order_list",ord);
        model.addAttribute("total",total_price);


        return "userpg/orders";
    }
    @PostMapping("/invoice/{id}")
    public ResponseEntity<?> invoice_generator(@PathVariable("id") String  id) {
        try {
            // Fetch orders by invoice_id
            List<OrderTableEN> orders = orderManage.findByOrderid(id);
            if (orders == null || orders.isEmpty()) {
                return ResponseEntity.status(404).body("No orders found for invoice ID: " + id);
            }

            // Generate PDF
            byte[] pdfBytes = CustomerInvoice.generateCustomerInvoice(orders);
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=seller-invoice-" + id + ".pdf");
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.add(HttpHeaders.PRAGMA, "no-cache");
            headers.add(HttpHeaders.EXPIRES, "0");

            // Return PDF response
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            // Log error (in production, use SLF4J)
            System.err.println("Error generating invoice: " + e.getMessage());
            return ResponseEntity.status(500).body("Error generating invoice: " + e.getMessage());
        }
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