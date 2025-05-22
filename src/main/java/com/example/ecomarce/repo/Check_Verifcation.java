package com.example.ecomarce.repo;

import com.example.ecomarce.entity.Common_UserEN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface Check_Verifcation extends JpaRepository<Common_UserEN, String> {

    @Query("SELECT c.is_verified FROM Common_UserEN c WHERE c.username = :username")
    Boolean findVerificationStatusByEmail(@Param("username") String username);

    @Query("SELECT c.id FROM Common_UserEN c WHERE c.username = :username")
    int findID(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("UPDATE Common_UserEN c SET c.is_verified = :isVerified WHERE c.username = :username")
    int updateVerificationStatus(@Param("username") String username, @Param("isVerified") boolean isVerified);

    default boolean verifyCustomerByEmail(String username,boolean verficationStatus) {
        int affectedRows = updateVerificationStatus(username,verficationStatus);
        return affectedRows > 0;
    }





}
