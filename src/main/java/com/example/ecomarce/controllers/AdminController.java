package com.example.ecomarce.controllers;

import com.example.ecomarce.entity.Common_UserEN;
import com.example.ecomarce.entity.ImageEN;
import com.example.ecomarce.entity.OrderTableEN;
import com.example.ecomarce.entity.ProductEN;

import com.example.ecomarce.helper.MessAge;

import com.example.ecomarce.pdf_maker_class.InvoiceGenerator;
import com.example.ecomarce.repo.Order_Manage;
import com.example.ecomarce.repo.ProDuct_repo;
import com.example.ecomarce.repo.adminrepo.Change_role_repo;
import com.example.ecomarce.service_pkg.Order_Manage_Service;
import com.example.ecomarce.service_pkg.adminservice.RoleChangerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private Order_Manage_Service  order_service;




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
    @PostMapping("/admin/update")
    public String updateProduct(@ModelAttribute("product_details") ProductEN product, @RequestParam("images") MultipartFile[] images) {

        System.out.println("Admin addition data  " +product);
        // Update product and images
        return "redirect:/admin/products";
    }
    @PostMapping("/update-req/{id}")
    public String updateProductData(@PathVariable("id") int id,@ModelAttribute("update_p") ProductEN product ,Model model) {

        System.out.println("product ID  "+ id);

       ProductEN productEN = proDuct_repo.findByProduct_id(id);
       model.addAttribute("update_p", productEN);
       model.addAttribute("check_id",id);
        // Update product and images
        return "adminpg/product-update";
    }

    @GetMapping("/products")
    public String products(Model model) {
        List<ProductEN> products_list = proDuct_repo.findAll();

        model.addAttribute("products", products_list);
        model.addAttribute("product_details", new ProductEN());
        model.addAttribute("update_p", new ProductEN());
        return "adminpg/admin_products";

    }



    @PostMapping("/invoice/{id}")
    public ResponseEntity<?> invoice_generator(@PathVariable("id") String  id) {
        try {
            // Fetch orders by invoice_id
            List<OrderTableEN> orders = order_manage.findByOrderid(id);
            if (orders == null || orders.isEmpty()) {
                return ResponseEntity.status(404).body("No orders found for invoice ID: " + id);
            }

            // Generate PDF
            byte[] pdfBytes = InvoiceGenerator.generateSellerInvoice(orders);
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

    @PostMapping("/order-status/{id}")
    public String order_Status(@PathVariable("id") String  id,
                               @RequestParam("order_status") Boolean order_status,
                                Model model) {
        List<OrderTableEN> orders = order_manage.findByOrderid(id);

        String status;
        System.out.println(order_status);
        if (order_status) {
            status = "Delivered";
        }else  {
            status = "cancelled";
        }


        for (OrderTableEN order : orders) {

            if (status.equals("Delivered")){
                LocalDateTime time = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy, hh:mm a");

                String formattedTime = time.format(formatter);
                order_service.Update_Order_Status(order.getInvoice_id(),status,"Paid",order.getOrder_payment_method(),formattedTime);
            } else {
                LocalDateTime time = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy, hh:mm a");

                String formattedTime = time.format(formatter);
                order_service.Update_Order_Status(order.getInvoice_id(),status,"cancelled",order.getOrder_payment_method() ,formattedTime);
            }


        }

        return "redirect:/admin/lg";
    }

    @PostMapping("/payment-vfy/{id}")
    public String payment_Status(@PathVariable("id") String  id,
                               @RequestParam("payment_status") Boolean payment_status,
                               Model model) {
        List<OrderTableEN> orders = order_manage.findByOrderid(id);

        String status;
        System.out.println(payment_status);
        if (payment_status) {
            status = "COD";
        }else  {
            status = "Paid";
        }


        for (OrderTableEN order : orders) {

            order_service.Update_payment_Status(order.getInvoice_id(),status);

        }

        return "redirect:/admin/lg";
    }


    @GetMapping("/lg")
    public String admin(Model model) {


        List<Common_UserEN> user = change_role_repo.findAll();
        List<Common_UserEN> userdatamap = new ArrayList();
        List<OrderTableEN> all_order_list = order_manage.fatchallorder();



        List<OrderTableEN> all_uniq_invoice_order = new ArrayList();
        float total_sell=0;
        float total_subtotal=0;
        int how_many_product_sell=0;
        List<OrderTableEN> all_uniq_order_invoice = order_manage.Select_all_order_by_status("Delivered");
        for (OrderTableEN order_count : all_uniq_order_invoice) {
            float calculate_profit = 0;
            calculate_profit = order_count.getProducten().getSelling_price() - order_count.getProducten().getBuying_price();
            total_sell = total_sell + calculate_profit;
            total_subtotal+= order_count.getOrder_subtotal();
            how_many_product_sell ++;
            System.out.println("total financial status " +order_count.getProducten().getProduct_id() +"   "+"profit "+ calculate_profit +"  "+ "Total profit  " + total_sell )  ;
        }
        System.out.println("\n\ntotal sell "+  how_many_product_sell +"Total subtotal  "+ total_subtotal);

        Set<String> seenInvoiceIds = new HashSet<>();

        for (OrderTableEN order : all_order_list) {
            String invoiceId = order.getInvoice_id();
            if (invoiceId != null && !seenInvoiceIds.contains(invoiceId)) {
                all_uniq_invoice_order.add(order);
                seenInvoiceIds.add(invoiceId);
            }
        }

            for (Common_UserEN link : user) {
            try {
                if (!link.getRole().equals("ROLE_USER")) {
                    userdatamap.add(link);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        model.addAttribute("order_list" , all_uniq_invoice_order);
        model.addAttribute("total_sell" , total_subtotal);
        model.addAttribute("total_profit" , total_sell);
        model.addAttribute("total_sold_product" , how_many_product_sell);

        model.addAttribute("userdatamap", userdatamap);
        return "adminpg/admin";

    }

}
