package com.example.ecomarce.repo;


import com.example.ecomarce.entity.Common_UserEN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserAuth extends JpaRepository<Common_UserEN, Integer> {
    Common_UserEN findByUsername(String username);




}
