package com.example.ecomarce.service_pkg;

import com.example.ecomarce.entity.Common_UserEN;

import com.example.ecomarce.repo.UserAuth;
import com.example.ecomarce.security_config.UserDetails_sC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;


@Service
public class UserDetailsIML implements UserDetailsService {
    @Autowired
    private UserAuth userAuth;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Common_UserEN adminEN = userAuth.findByUsername(username);
        if (adminEN == null) {

            throw new UsernameNotFoundException(username);
        }
       return new UserDetails_sC(adminEN);
    }


}
