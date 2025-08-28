package com.example.ecomarce.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecomarce.entity.Common_UserEN;

@Repository
public interface Check_Verifcation extends JpaRepository<Common_UserEN, Integer> {

    @Query("SELECT c.is_verified FROM Common_UserEN c WHERE c.username = :username")
    Boolean findVerificationStatusByEmail(@Param("username") String username);

    @Query("SELECT c.device_ip_one FROM Common_UserEN c WHERE c.username = :username")
    String findDeviceIP_one_ByEmail(@Param("device_ip_one") String device_ip_one,
                                     @Param("username") String username);
    @Query("SELECT c.device_ip_two FROM Common_UserEN c WHERE c.username = :username")
    String findDeviceIP_two_ByEmail(@Param("device_ip_two") String device_ip_two,
                                     @Param("username") String username);

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


    @Transactional
    @Modifying
    @Query("UPDATE Common_UserEN o SET o.device_ip_one = :deviceIP_one ,o.device_one = :device_one  WHERE o.username = :username")
    int deviceIP_one(
            @Param("deviceIP_one") String deviceIP_one,
            @Param("device_one") String device_one,
            @Param("username") String username
    );
    default boolean deviceIP_one_update(String email,String device_one, String deviceIP_one) {
        System.out.println(email +"  " +device_one +" reposetory" + "  "+deviceIP_one);
        int affectedRows = deviceIP_one(deviceIP_one,device_one,email);
        return affectedRows > 0;
    }

    @Transactional
    @Modifying
    @Query("UPDATE Common_UserEN o SET o.device_ip_two = :device_IP_two ,o.device_two = :device_two  WHERE o.username = :username")
    int deviceIP_two(
            @Param("device_IP_two") String device_IP_two,
            @Param("device_two") String device_two,
            @Param("username") String username
    );
    default boolean deviceIP_two_update(String email,String device_two ,String device_IP_two) {
        int affectedRows = deviceIP_two(device_IP_two,device_two,email);
        return affectedRows > 0;
    }
}
