package com.example.ecomarce.repo;


import com.example.ecomarce.entity.OrderTableEN;
import com.example.ecomarce.entity.ProductEN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Repository
public interface Order_Manage extends JpaRepository<OrderTableEN, Integer> {

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM OrderTableEN o WHERE o.invoice_id = :invoice_id")
    Boolean findinvoiceId(@Param("invoice_id") String invoice_id);

    @Query("SELECT c FROM OrderTableEN c")
    List<OrderTableEN> fatchallorder();

    @Query("SELECT o FROM OrderTableEN o WHERE o.invoice_id = :invoice_id")
    List<OrderTableEN> findByOrderid(@Param("invoice_id") String invoice_id );

    @Query("SELECT o FROM OrderTableEN o WHERE o.user.id = :user_id")
    List<OrderTableEN> findOrderList(@Param("user_id") int user_id );

    @Query("SELECT o FROM OrderTableEN o WHERE o.invoice_id = :invoice_id")
    List<OrderTableEN> findinvoice(@Param("invoice_id") String invoice_id );

//    @Query("SELECT o FROM OrderTableEN o WHERE o.order_id = :orderid AND o.producten.product_id = :product_id")
//    OrderTableEN findOrderSetForRating(@Param("orderid") int orderid,
//                                      @Param("product_id") int product_id);


    @Query("SELECT o FROM OrderTableEN o WHERE o.producten.product_id = :product_id")
    List<OrderTableEN> findallproduct(@Param("product_id") int product_id );
    @Transactional
    @Modifying
    @Query("UPDATE OrderTableEN o SET  o.rating = :rating  WHERE o.order_id = :order_id")
    int updateOrderRatng(
            @Param("order_id") int order_id,
            @Param("rating") int rating
    );

    default boolean updateOrder_rate(int order_id, int rating) {
        int affectedRows = updateOrderRatng(order_id, rating);
        return affectedRows > 0;
    }

    @Transactional
    @Modifying
    @Query("UPDATE OrderTableEN o SET o.order_status = :status, o.order_payment_status = :order_payment_status, o.order_payment_method = :order_payment_method  WHERE o.invoice_id = :invoice_id")
    int updateOrderStatus(
            @Param("invoice_id") String invoice_id,
            @Param("status") String status,
            @Param("order_payment_status") String order_payment_status,
            @Param("order_payment_method") String order_payment_method
    );

    default boolean updateOrderInfo(String invoice_id, String status, String order_payment_status, String order_payment_method) {
        int affectedRows = updateOrderStatus(invoice_id, status, order_payment_status, order_payment_method);
        return affectedRows > 0;
    }


    @Transactional
    @Modifying
    @Query("UPDATE OrderTableEN o SET o.order_payment_status = :stetus WHERE o.invoice_id = :invoice_id")
    int updatePaymentStatus(@Param("invoice_id") String invoice_id, @Param("stetus") String stetus);

    default boolean get_invoice_id_p(String invoice_id,String stetus) {
        int affectedRows = updatePaymentStatus(invoice_id,stetus);
        return affectedRows > 0;
    }

}
