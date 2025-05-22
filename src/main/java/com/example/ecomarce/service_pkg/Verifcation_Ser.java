package com.example.ecomarce.service_pkg;


import com.example.ecomarce.repo.Check_Verifcation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Verifcation_Ser {

    @Autowired
    private Check_Verifcation verifcation_ser;

    public boolean verifcation_check(String email) {

        return verifcation_ser.findVerificationStatusByEmail(email) ;
    }
    public int ID_reader(String email) {


        return verifcation_ser.findID(email) ;
    }
    @Transactional
    public boolean verify(String email,boolean verifcation_status) {

        return verifcation_ser.verifyCustomerByEmail(email,verifcation_status) ;
    }

}
