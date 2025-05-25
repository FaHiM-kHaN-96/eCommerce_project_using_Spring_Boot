package com.example.ecomarce.repo;


import com.example.ecomarce.entity.OrderTableEN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Order_Manage extends JpaRepository<OrderTableEN, Integer> {

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM OrderTableEN o WHERE o.invoice_id = :invoice_id")
    Boolean findinvoiceId(@Param("invoice_id") String invoice_id);
}
