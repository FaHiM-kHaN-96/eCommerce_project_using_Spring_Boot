package com.example.ecomarce.repo;


import com.example.ecomarce.entity.Common_UserEN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuth extends JpaRepository<Common_UserEN, Integer> {
    Common_UserEN findByUsername(String username);
}
