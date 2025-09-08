package com.example.ecomarce.repo;

import com.example.ecomarce.entity.Otp_EN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface Otp_Repo extends JpaRepository<Otp_EN, Integer>
{
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Otp_EN c WHERE c.otp_password = :Otp_password")
    Boolean existsByOtppass(@Param("Otp_password") String Otp_password);
//    @Query("SELECT o.Otp_password FROM Otp_EN o WHERE o.Otp_password = :Otp_password")
//    String return_otp(@Param("Otp_password") String Otp_password);
//    @Query("SELECT o.Otp_sender_userid FROM Otp_EN o WHERE o.Otp_sender_userid = :Otp_sender_userid")
//    String return_userID(@Param("Otp_sender_userid") String Otp_sender_userid);


    @Query("SELECT o.otp_sender_userid FROM Otp_EN o " +
            "WHERE o.otp_sender_userid = :Otp_sender_userid " +
            "AND o.otp_verifide = :Otp_verifide")
    String return_userID(@Param("Otp_sender_userid") String Otp_sender_userid,
                         @Param("Otp_verifide") boolean Otp_verifide);


    @Query("SELECT o.otp_password FROM Otp_EN o " +
            "WHERE o.otp_password = :Otp_password " +
            "AND o.otp_verifide = :Otp_verifide")
    String return_otp(@Param("Otp_password") String Otp_password,
                      @Param("Otp_verifide") boolean Otp_verifide);


    @Query("SELECT o.target_ip FROM Otp_EN o " +
            "WHERE o.otp_password = :Otp_password " +
            "AND o.otp_verifide = :Otp_verifide")
    String return_target(@Param("Otp_password") String Otp_password,
                      @Param("Otp_verifide") boolean Otp_verifide);



    @Transactional
    @Modifying
    @Query("UPDATE Otp_EN o SET o.otp_verifide = :Otp_verifide   WHERE o.otp_password = :Otp_password")
    int otp_verification(
            @Param("Otp_verifide") boolean Otp_verifide,
            @Param("Otp_password") String Otp_password
    );
    default boolean otp_verification_update(boolean Otp_verifide ,String Otp_password) {
        int affectedRows = otp_verification(Otp_verifide,Otp_password);
        return affectedRows > 0;
    }

}
