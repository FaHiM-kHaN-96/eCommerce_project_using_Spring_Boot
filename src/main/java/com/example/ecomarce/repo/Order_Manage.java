package com.example.ecomarce.repo;


import com.example.ecomarce.entity.OrderTableEN;
import com.example.ecomarce.entity.ProductEN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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

}
