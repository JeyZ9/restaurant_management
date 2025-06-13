package com.app.restaurant_management.seeders;

import com.app.restaurant_management.commons.enums.RoleName;
import com.app.restaurant_management.models.Role;
import com.app.restaurant_management.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Autowired
    public DatabaseSeeder(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args){
        if(roleRepository.count() == 0L){
            List<Role> roles = new ArrayList<>();
            roles.add(new Role(RoleName.USER));
            roles.add(new Role(RoleName.ADMIN));
            roles.add(new Role(RoleName.MANAGER));
            roles.add(new Role(RoleName.EMPLOYEE));
            roleRepository.saveAll(roles);
        }
    }
}
