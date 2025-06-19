package com.app.restaurant_management.seeders;

import com.app.restaurant_management.commons.enums.RoleName;
import com.app.restaurant_management.models.Menu;
import com.app.restaurant_management.models.Role;
import com.app.restaurant_management.repository.MenuRepository;
import com.app.restaurant_management.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;

    @Autowired
    public DatabaseSeeder(RoleRepository roleRepository, MenuRepository menuRepository){
        this.roleRepository = roleRepository;
        this.menuRepository = menuRepository;
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

        if(menuRepository.count() == 0L){
            List<Menu> menus = new ArrayList<>();
            menus.add(new Menu("ต้ม"));
            menus.add(new Menu("ผัด/ทอด"));
            menus.add(new Menu("ยำ"));
            menuRepository.saveAll(menus);
        }
    }
}
