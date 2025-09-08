//package com.example.ecomarce.service_pkg;
//
//import com.example.ecomarce.entity.Common_UserEN;
//import com.example.ecomarce.repo.UserAuth;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class DeviceRemoveservice {
//    private final UserAuth repository;
//
//    public DeviceRemoveservice(UserAuth repository) {
//        this.repository = repository;
//    }
//
//    public boolean deleteDeviceByIp(String deviceIp) {
//        // Try device_ip_one
//        Optional<Common_UserEN> userOpt = repository.findByDevice_ip_one(deviceIp);
//        if (userOpt.isPresent()) {
//            Common_UserEN user = userOpt.get();
//            user.setDevice_ip_one(null);
//            user.setDevice_one(null);
//            repository.save(user);
//            return true;
//        }
//
//        // Try device_ip_two
//        userOpt = repository.findByDevice_ip_two(deviceIp);
//        if (userOpt.isPresent()) {
//            Common_UserEN user = userOpt.get();
//            user.setDevice_ip_two(null);
//            user.setDevice_two(null);
//            repository.save(user);
//            return true;
//        }
//
//        return false; // not found
//    }
//}
