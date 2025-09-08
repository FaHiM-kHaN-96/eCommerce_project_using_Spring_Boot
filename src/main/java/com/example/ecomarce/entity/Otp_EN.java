package com.example.ecomarce.entity;

import jakarta.persistence.*;



@Entity
@Table(name = "otp_history")
public class Otp_EN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int otp_ID;
    @Column(unique = true, nullable = false)
    private String otp_password;
    private boolean otp_verifide;
    private Integer otp_sender_userid;
    private String target_ip;
    private boolean otp_sending_reason  ;  // reason true means otp for remove device and false means otp for forget password

    @Override
    public String toString() {
        return "Otp_EN{" +
                "otp_ID=" + otp_ID +
                ", otp_password='" + otp_password + '\'' +
                ", otp_verifide=" + otp_verifide +
                ", otp_sender_userid=" + otp_sender_userid +
                ", target_ip='" + target_ip + '\'' +
                ", otp_sending_reason=" + otp_sending_reason +
                '}';
    }

    public int getOtp_ID() {
        return otp_ID;
    }

    public void setOtp_ID(int otp_ID) {
        this.otp_ID = otp_ID;
    }

    public String getOtp_password() {
        return otp_password;
    }

    public void setOtp_password(String otp_password) {
        this.otp_password = otp_password;
    }

    public boolean isOtp_verifide() {
        return otp_verifide;
    }

    public void setOtp_verifide(boolean otp_verifide) {
        this.otp_verifide = otp_verifide;
    }

    public Integer getOtp_sender_userid() {
        return otp_sender_userid;
    }

    public void setOtp_sender_userid(Integer otp_sender_userid) {
        this.otp_sender_userid = otp_sender_userid;
    }

    public String getTarget_ip() {
        return target_ip;
    }

    public void setTarget_ip(String target_ip) {
        this.target_ip = target_ip;
    }

    public boolean isOtp_sending_reason() {
        return otp_sending_reason;
    }

    public void setOtp_sending_reason(boolean otp_sending_reason) {
        this.otp_sending_reason = otp_sending_reason;
    }
}
