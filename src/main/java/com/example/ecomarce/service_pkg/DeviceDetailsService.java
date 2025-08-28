package com.example.ecomarce.service_pkg;

import com.example.ecomarce.entity.Common_UserEN;
import com.example.ecomarce.repo.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class DeviceDetailsService {
    @Autowired
    private UserAuth userAuth;

    public String device_name() throws UnknownHostException {

        // Get the local host (your device)
        InetAddress localHost = InetAddress.getLocalHost();

        // Get hostname
        String hostName = localHost.getHostName();


        if (hostName==null){

            System.out.println("Host name is null "+hostName);
        } else if (hostName!=null) {
            System.out.println(hostName);
            return hostName;
        }


        return null;
    }

    public String device_ip() throws UnknownHostException {
        // Get the local host (your device)
        InetAddress localHost = InetAddress.getLocalHost();

        // Get IP address
        String ipAddress = localHost.getHostAddress();
        if (ipAddress!=null){
            System.out.println(ipAddress);

            return ipAddress+".90";
        } else if (ipAddress==null) {
            System.out.println("Ip name is null "+ipAddress);
        }
        System.out.println("IP Address: " + ipAddress);
        return null;

    }


//    public boolean device_ip_one_check(Common_UserEN userdetails_get_by_main_ctrl) throws UnknownHostException {
//        boolean ip_one_check = false;
//        // Get the local host (your device)
//        InetAddress localHost = InetAddress.getLocalHost();
//
//        // Get hostname
//        String hostName = localHost.getHostName();
//
//        // Get IP address
//        String ipAddress = localHost.getHostAddress();
//
//        System.out.println("IP Address: " + ipAddress);
//        if (userdetails_get_by_main_ctrl.getDevice_ip_one()!=null){
//                if (ipAddress.equals(userdetails_get_by_main_ctrl.getDevice_ip_one()) ) {
//                ip_one_check = true;
//                }
//
//        }
//
//        return  ip_one_check;
//
//    }
//    public boolean device_ip_two_check(Common_UserEN userdetails_get_by_main_ctrl) throws UnknownHostException {
//        boolean ip_two_check = false;
//        // Get the local host (your device)
//        InetAddress localHost = InetAddress.getLocalHost();
//
//        // Get hostname
//        String hostName = localHost.getHostName();
//
//        // Get IP address
//        String ipAddress = localHost.getHostAddress();
//
//        System.out.println("IP Address: " + ipAddress);
//
//        if (!ipAddress.equals(userdetails_get_by_main_ctrl.getDevice_ip_two()) ) {
//            ip_two_check = true;
//        }
//        return  ip_two_check;
//
//    }
}
