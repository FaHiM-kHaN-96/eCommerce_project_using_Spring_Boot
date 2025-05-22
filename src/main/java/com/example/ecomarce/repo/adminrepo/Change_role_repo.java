package com.example.ecomarce.repo.adminrepo;


import com.example.ecomarce.entity.Common_UserEN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface Change_role_repo extends JpaRepository<Common_UserEN,String> {

    @Query("SELECT c FROM Common_UserEN c")
    List<Common_UserEN> findAll();


    @Transactional
    @Modifying
    @Query("UPDATE Common_UserEN c SET c.role = :role WHERE c.username = :username")
    int updateRole(@Param("username") String username, @Param("role") String role);

    default boolean Role_setter(String username,String role) {
        int affectedRows = updateRole(username,role);
        return affectedRows > 0;
    }
}
