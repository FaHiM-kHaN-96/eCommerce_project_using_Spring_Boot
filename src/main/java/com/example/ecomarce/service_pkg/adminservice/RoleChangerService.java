package com.example.ecomarce.service_pkg.adminservice;

import com.example.ecomarce.repo.adminrepo.Change_role_repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleChangerService {
    @Autowired

     private Change_role_repo change_role_repo;
    @Transactional
    public boolean ChangeRole(String email,String role) {

        return change_role_repo.Role_setter(email,role);
    }
}

