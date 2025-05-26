package com.example.ecomarce.repo;


import com.example.ecomarce.entity.OrderTableEN;
import com.example.ecomarce.entity.ProductEN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface Order_Manage extends JpaRepository<OrderTableEN, Integer> {

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM OrderTableEN o WHERE o.invoice_id = :invoice_id")
    Boolean findinvoiceId(@Param("invoice_id") String invoice_id);

    @Query("SELECT o FROM OrderTableEN o WHERE o.order_status = :order_status")
    List<OrderTableEN> findByStatus(@Param("order_status") String order_status );

    @Query("SELECT o FROM OrderTableEN o WHERE o.order_id = :order_id")
    OrderTableEN findByOrderid(@Param("order_id") int order_id );

}
