package com.example.ecomarce.repo;

import com.example.ecomarce.entity.Common_UserEN;
import com.example.ecomarce.entity.ProductEN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ProDuct_repo extends JpaRepository<ProductEN,String> {
    @Query("SELECT c FROM ProductEN c")
    List<ProductEN> findAll();

    @Transactional
    @Modifying
    @Query("UPDATE ProductEN p SET  p.product_avg_rating = :product_avg_rating  WHERE p.product_id = :product_id")
    int updateproductRatng(
            @Param("product_id") int product_id,
            @Param("product_avg_rating") double product_avg_rating
    );

    default boolean set_rating(int product_id, double product_avg_rating) {
        int affectedRows = updateproductRatng(product_id, product_avg_rating);
        return affectedRows > 0;
    }

    @Query("SELECT c FROM ProductEN c WHERE c.product_id = :id")
    ProductEN findByProduct_id(@Param("id") int id);



}
