
package com.smartdesk.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.smartdesk.service.UserRoleService;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRoleService userRoleService;

    public DataInitializer(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Override
    public void run(String... args) throws Exception {

        userRoleService.createRole("USER");
        userRoleService.createRole("ADMIN");

    }
}
