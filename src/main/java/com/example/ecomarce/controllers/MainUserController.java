package com.example.ecomarce.controllers;

import com.example.ecomarce.entity.Otp_EN;
import com.example.ecomarce.repo.*;
import com.example.ecomarce.service_pkg.*;
import com.example.ecomarce.entity.Common_UserEN;
import com.example.ecomarce.entity.OrderTableEN;
import com.example.ecomarce.entity.ProductEN;
import com.example.ecomarce.generic_logic.PinGenerator;
import com.example.ecomarce.helper.MessAge;
import com.example.ecomarce.logical_class.Cart_Add;
import com.example.ecomarce.pdf_maker_class.CustomerInvoice;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    @Autowired
    private Check_Verifcation checkip;
    @Autowired
    private DeviceDetailsService deviceDetailsService;

    @Autowired
    private Otp_Repo otpRepo;

    @Autowired
    private OtpUtil otpUtil;
//    @Autowired
//    private DeviceRemoveservice deviceService;

    private final List<Cart_Add> cartlist = new ArrayList<>();
    @Autowired
    private Otp_Repo otp_Repo;


    // forget password data variable start
     private String forget_otp;
     private int userid;

    public String getForget_otp() {
        return forget_otp;
    }

    public void setForget_otp(String forget_otp) {
        this.forget_otp = forget_otp;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
// forget password data variable end

    @GetMapping("/login")
    public String login() {



        return "userpg/login";
    }


    @GetMapping("/")
    public String product( Model model) {
        // String email = principal.getName();
        List<ProductEN> productlist = proDuct_repo.findAll();

        List<ProductEN> active_products = new ArrayList<>();

        double averageRating = 0.0;
        for (ProductEN product : productlist) {
            if (product.isProduct_active()){
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

                active_products.add(product);
            }
        }


        int count = cartlist.size();
        String number = String.valueOf(count);
        System.out.println("All cart  "+ number);
        model.addAttribute("cart_dt", number);
        model.addAttribute("imagelst", active_products);
        model.addAttribute("productlist", active_products);
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
        String link = "https://f4c6437cacb4.ngrok-free.app/verification/"+email;
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
        if (checkVerifcation.verifcation_check(principal.getName())) {
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

                    Common_UserEN UserThankYouEmail =  userAuth.findByUsername(principal.getName());
                    emailService.sendThankYouEmail(principal.getName(),UserThankYouEmail.getFullname(),orderTableEN.getInvoice_id());
                }

                cartlist.clear();
                session.setAttribute("message_check",new MessAge("Order Processed Successfully","success-message") );

                return "userpg/order_status";
            } catch (Exception e) {

                session.setAttribute("message_check",new MessAge("Order Failed"+e.getMessage(),"error-message") );

            }

// All courted all product should be uploaded on database with single inv number
            return "userpg/order_status";
        }else {


            return "redirect:/verify";
        }



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

    private boolean ip_one_compare ( String email) throws UnknownHostException {

       String database_ip1 = checkip.findDeviceIP_one_ByEmail(deviceDetailsService.device_ip(),email);
        System.out.println("comare fun ip address "+ database_ip1);
        System.out.println( "\nderaect function " + deviceDetailsService.device_ip());
       if (database_ip1 != null) {
           return database_ip1.equals(deviceDetailsService.device_ip());
       }

        return false;
    }
    private boolean ip_two_compare ( String email) throws UnknownHostException {

        String database_ip2 = checkip.findDeviceIP_two_ByEmail(deviceDetailsService.device_ip(),email);
        if (database_ip2 != null) {
            return database_ip2.equals(deviceDetailsService.device_ip());
        }

        return false;
    }


    @GetMapping("/verify")
    public String verify(Principal principal,HttpSession session,Model model) throws UnknownHostException {

        if (checkVerifcation.verifcation_check(principal.getName())){

            if (checkip.findDeviceIP_one_ByEmail(deviceDetailsService.device_ip(),principal.getName()) == null) {
                try{
                    if (checkip.deviceIP_one_update(principal.getName(), deviceDetailsService.device_name(), deviceDetailsService.device_ip())){
                        System.out.println("device 1 details update ");
                        return "redirect:/";
                    }

                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("!=  findDeviceIP_one_ByEmail");

            } else {
                if(ip_one_compare(principal.getName()))
                {
                    System.out.println("Match comp in ip one condition");
                    return "redirect:/";


                } else  {
                    if(!ip_two_compare(principal.getName()) && checkip.findDeviceIP_two_ByEmail(deviceDetailsService.device_ip(),principal.getName()) == null)
                    {
                        if (checkip.deviceIP_two_update(principal.getName(), deviceDetailsService.device_name(), deviceDetailsService.device_ip())){
                            System.out.println("device 2 details update ");
                            return "redirect:/";
                        }

                    }else {

                        if(ip_two_compare(principal.getName())||ip_one_compare(principal.getName()))
                        {
                            return "redirect:/";

                        }else {



//                            try {
//                                session.setAttribute("message3",new MessAge("you are using more then two device for single account","alert-warning") );
//                            } catch (Exception e) {
//                                session.setAttribute("message3",new MessAge(e.getMessage(),"alert-error") );
//
//                            }
                            Common_UserEN commonUserEN =  userAuth.findByUsername(principal.getName());

                            model.addAttribute("device", commonUserEN);
                            return "userpg/device_display";
                        }
                    }


                }

            }
            System.out.println("ip failed");
            return "redirect:/";
        }else {
            emaiilSender(principal.getName());
            return "userpg/verify";
        }

    }
    @PostMapping("/device-delete/{deviceIp}")  // ✅ method-level mapping
    public String deletsc(@PathVariable String deviceIp,Principal principal) {
        //here all credential made by principal
        System.out.println("hbscabchwsbha "+deviceIp+ "  " + checkip.finduserid(principal.getName()));

        String ip_one= checkip.findDeviceIP_one_ByEmailForRemove(principal.getName());
        String ip_two= checkip.findDeviceIP_two_ByEmailForRemove(principal.getName());
        if (ip_one.equals(deviceIp)) {
            try {
                Otp_EN otpen_one = new Otp_EN();
                String otp_pass_ip_one = otpUtil.generate6DigitOtp();
                otpen_one.setOtp_password(otp_pass_ip_one);
                otpen_one.setOtp_sender_userid(checkip.finduserid(principal.getName()));
                otpen_one.setTarget_ip(deviceIp);
                otpen_one.setOtp_sending_reason(true);
                System.out.println(otpen_one);
                device_removin_email(otp_pass_ip_one,principal.getName(),checkip.finduserid(principal.getName()));
                otpRepo.save(otpen_one);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println("ip one matched ");
        }else if (ip_two.equals(deviceIp)) {
            try {
                String otp_pass_ip_two = otpUtil.generate6DigitOtp();
                Otp_EN otpen = new Otp_EN();
                otpen.setOtp_password(otp_pass_ip_two);
                otpen.setOtp_sender_userid(checkip.finduserid(principal.getName()));
                otpen.setTarget_ip(deviceIp);
                otpen.setOtp_sending_reason(true);
                System.out.println(otpen);
                device_removin_email(otp_pass_ip_two,principal.getName(),checkip.finduserid(principal.getName()));
                otpRepo.save(otpen);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            System.out.println("ip two matched ");
        }

//        System.out.println("check two ip's \n"+ "ip one  "++"\n" +" ip two  "+ checkip.findDeviceIP_two_ByEmailForRemove(principal.getName()));

        return "userpg/verify";
    }

    private void device_removin_email(String ip,String email,int id) {

        String link = "https://f4c6437cacb4.ngrok-free.app/delete-ip-vfy/"+ip+"/"+id;
        emailService.sendRemoveDeviceVerificationEmail(email,link);


    }
    @GetMapping("/delete-ip-vfy/{otp}/{id}")
    public String delete_ip(@PathVariable("otp") String otp,@PathVariable("id") String id,Principal principal,HttpSession session) {
        System.out.println("\n\n\nipts   "+otp);
        System.out.println("Link address otp  "+otp_Repo.return_otp(otp , false)+"UserId "+id);


        try {
            if (otp_Repo.return_otp(otp,false).equals(otp) && otp_Repo.return_userID(id,false).equals(id) ) {

                if (checkip.findDeviceIP_one_ByEmailForRemove(principal.getName()).equals(otp_Repo.return_target(otp,false))) {

                    if (otp_Repo.otp_verification_update(true,otp)){

                        if (checkip.clearDeviceInfoOneByUsername(principal.getName())){
                            session.setAttribute("message3",new MessAge(checkip.findDeviceIP_one_ByEmailForRemove(principal.getName())+" This device removed successfully ","alert-success") );
                            System.out.println("Delete IP One");
                            return "userpg/login_status";
                        }


                    }

                } else if (checkip.findDeviceIP_two_ByEmailForRemove(principal.getName()).equals(otp_Repo.return_target(otp,false))) {
                    if (checkip.clearDeviceInfoTwoByUsername(principal.getName())){
                        session.setAttribute("message3",new MessAge(checkip.findDeviceIP_two_ByEmailForRemove(principal.getName())+" This device removed successfully ","alert-success") );
                        System.out.println("Delete IP two");
                        return "userpg/login_status";
                    }

                }


            }
        } catch (Exception e) {
            session.setAttribute("message3",new MessAge("device not found ","alert-error") );

        }


        return "userpg/login_status";

    }

    @GetMapping("/forget-vfy")
    public String forget_vfy_s() {

        return "userpg/forget_password";

    }

    @GetMapping("/forget-vfy/reset-pass")
    public String reset_pass() {

        return "userpg/reset-password";

    }


    @PostMapping("/forget-vfy/get-inp")
    public ResponseEntity<String> forget_vfy_email(@RequestParam("email") String email, HttpSession session ) {

    if(checkip.existsByUsername(email)){

        try {
            System.out.println("\n\n\nforget-vfy  "+email+" User ID  "+  checkip.findID(email));

                String otp_forget_password = otpUtil.generate6DigitOtp();
                Otp_EN otpen = new Otp_EN();
                otpen.setOtp_password(otp_forget_password);
                otpen.setOtp_sender_userid(checkip.findID(email));
                otpen.setTarget_ip(email);
                otpen.setOtp_sending_reason(false);
                setForget_otp(otp_forget_password);
                setUserid(checkip.findID(email));
                System.out.println(otpen);
                forget_password_email(otp_forget_password,email);
                otpRepo.save(otpen);

                return ResponseEntity.ok("OTP Send successfully ");
                //  here set 60 secound timer
            } catch (Exception e) {

                session.setAttribute("message2",new MessAge("Sending Failed"+e.getMessage(),"alert-error") );

            }
        }


        return ResponseEntity.badRequest().body("Invalid email!! user not found");

    }

    private void forget_password_email(String otp,String email) {


        emailService.sendOtpEmail(email,otp);


    }
//    @PostMapping("/forget-vfy/countdown-start")
//

    @PostMapping("/forget-vfy/reset-password/")
    public String reset_password(@RequestParam("confarmpassword") String confarmpassword ,HttpSession session ) {


        System.out.println("Rest password and userid "+confarmpassword+" "+getUserid());


        try{

            if (checkip.updatepassword_byemail(getUserid(),new BCryptPasswordEncoder(12).encode(confarmpassword))) {
                session.setAttribute("message_forget",new MessAge("Password reset successfully","success-message") );
                return "userpg/alert_page";

            }


        } catch (Exception e) {
            session.setAttribute("message_forget",new MessAge(e.getMessage(),"error-message") );
        }


        return "userpg/alert_page";


    }

    @PostMapping("/forget-vfy/new-pass/{otp}")
    public String newpass_inputer(@PathVariable("otp") String otp) {

        String dbOtp = otp_Repo.return_otp_with_userid(otp, true,userid);
        if (dbOtp.equals(otp)) {
            return "userpg/new_password_inputer";
        }

        return "userpg/verify";
    }
    @PostMapping("/forget-vfy/otp-vfy/{otp}")
    public ResponseEntity<String> forget_vfy_otp(@PathVariable("otp") String otp,
                                                 @RequestBody Map<String, String> payload) {

        String email = payload.get("email");
        System.out.println("Client sent OTP: " + otp + " for email: " + email);
        int userid = checkip.findID(email);

        setUserid(userid);

        String dbOtp = otp_Repo.return_otp_with_userid(otp, false,userid);

        if (dbOtp != null && dbOtp.equals(otp)) {


            // ✅ OTP matched

            if (otp_Repo.otp_verification_update(true,otp)) {

                System.out.println("✅ OTP verified successfully!");
            }

            return ResponseEntity.ok("OTP verification successful");
        } else {
            // ❌ OTP invalid
            System.out.println("❌ OTP verification failed!");
            return ResponseEntity.badRequest().body("Invalid verification code");
        }
    }



    @PostMapping("/forget-vfy/countdown-start")
    @ResponseBody
    public ResponseEntity<String> countdownFinished() {
        System.out.println("Countdown started for OTP verification...");

        try {
            for (int i = 60; i > 0; i--) {
                System.out.println("Time left: " + i + " seconds");
                System.out.println("Checking OTP for user: " + getUserid() + ", OTP: " + getForget_otp());


                if (otpRepo.existsByOtpPassword(getForget_otp())) {
                    boolean verified = otpRepo.return_verification(getForget_otp(), getUserid(), false);
                    if (verified) {
                        System.out.println("✅ OTP verified successfully at " + (60 - i + 1) + " second!");
                        return ResponseEntity.ok("OTP verified");
                    } else {
                        System.out.println("❌ OTP not found or not verified yet.");
                        Thread.sleep(1000); // প্রতি সেকেন্ডে চেক করবে
                    }

                }else {
                    break;
                }


            }

            // ❌ Time’s up → delete OTP
            int deletedRows = otpRepo.deleteByOtpPassword(getForget_otp());
            if (deletedRows > 0) {
                setForget_otp(null);
                System.out.println("OTP deleted successfully!");
            } else {
                System.out.println("No OTP found to delete!");
            }

            return ResponseEntity.status(408).body("OTP verification failed - Timeout (60s passed)");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }






    @GetMapping("/verification/{email}")
    public String verification(@PathVariable("email") String email) {
        System.out.println("\n\n\nMail cheker  "+email);

        if (checkVerifcation.verify(email,true)) {
            return "redirect:/";
        }else {
            return "verification failed";

        }

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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.class)
    public String handleNotFound(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
  



}