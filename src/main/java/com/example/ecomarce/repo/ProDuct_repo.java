package com.example.ecomarce.repo;

import com.example.ecomarce.entity.Common_UserEN;
import com.example.ecomarce.entity.ProductEN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ProDuct_repo extends JpaRepository<ProductEN,String> {
    @Query("SELECT c FROM ProductEN c")
    List<ProductEN> findAll();



    @Query("SELECT c FROM ProductEN c WHERE c.product_id = :id")
    ProductEN findByProduct_id(@RequestParam("id") int id);



}
