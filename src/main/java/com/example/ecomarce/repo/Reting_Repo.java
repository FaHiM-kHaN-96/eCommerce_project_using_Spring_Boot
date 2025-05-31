package com.example.ecomarce.repo;

import com.example.ecomarce.entity.OrderTableEN;
import com.example.ecomarce.entity.Product_RatingEN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Reting_Repo extends JpaRepository<Product_RatingEN, Integer>{


}
