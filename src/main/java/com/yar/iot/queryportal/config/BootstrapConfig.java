package com.yar.iot.queryportal.config;

import com.yar.iot.queryportal.common.request.user.UserRequest;
import com.yar.iot.queryportal.domain.document.UserDocument;
import com.yar.iot.queryportal.domain.enums.Authority;
import com.yar.iot.queryportal.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * The application bootstarp configuration
 * */
@Configuration
@AllArgsConstructor
public class BootstrapConfig {

    private static final String ADMIN_USERNAME_KEY = "app.security.admin.username";
    private static final String ADMIN_PASSWORD_KEY = "app.security.admin.password";
    private static final String ADMIN_EMAIL_KEY = "app.security.admin.email";
    private static final String ADMIN_NAME_KEY = "app.security.admin.name";
    private static final String ADMIN_LASTNAME_KEY = "app.security.admin.lastname";

    private final UserService userService;
    private final Environment environment;

    /**
     * Initialize the default admin user
     */
    @Bean
    public CommandLineRunner initDefaultAdminUserAndPrivilegeRunner() {
        String adminUsername = environment.getRequiredProperty(ADMIN_USERNAME_KEY);
        return args -> {
            UserDocument adminUser;
            if (!userService.userExists(adminUsername)) {
                UserRequest adminRequest = new UserRequest();
                adminRequest.setAuthority(Authority.ADMIN);
                adminRequest.setUsername(adminUsername);
                adminRequest.setPassword(environment.getRequiredProperty(ADMIN_PASSWORD_KEY));
                adminRequest.setEmail(environment.getRequiredProperty(ADMIN_EMAIL_KEY));
                adminRequest.setName(environment.getRequiredProperty(ADMIN_NAME_KEY));
                adminRequest.setLastname(environment.getRequiredProperty(ADMIN_LASTNAME_KEY));
                userService.add(adminRequest);
            } else  {
                adminUser = userService.loadUserByUsername(adminUsername);
                if (!adminUser.isEnabled()) {
                    adminUser = userService.loadUserByUsername(adminUsername);
                    userService.activation(adminUser.getId(), Boolean.TRUE);
                }
            }
        };
    }

}
