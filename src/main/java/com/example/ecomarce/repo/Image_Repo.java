package com.example.ecomarce.repo;

import com.example.ecomarce.entity.ImageEN;
import com.example.ecomarce.entity.ProductEN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface Image_Repo extends JpaRepository<ImageEN, Integer> {



}
