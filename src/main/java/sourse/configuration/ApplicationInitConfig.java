package sourse.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import sourse.entity.Role;
import sourse.entity.User;
import sourse.enums.EnumType;
import sourse.repository.RoleRepository;
import sourse.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;


@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    @Bean
    ApplicationRunner applicationRunner(RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByEmail("superuser@superuser.com").isEmpty()) {
                Role superuserRole = roleRepository
                        .findByName(EnumType.UserRole.SUPERUSER.name())
                        .orElseGet(() -> {

                            Role newRole = new Role();
                            newRole.setName(EnumType.UserRole.SUPERUSER.name());
                            return roleRepository.save(newRole);
                        });

                User user = User.builder()
                        .email("superuser@superuser.com")
                        .password(passwordEncoder.encode("superuser123"))
                        .roles(Set.of(superuserRole))
                        .build();

                userRepository.save(user);
                System.out.println("created admin");
                log.warn("default create 1 user with email superuser@superuser.com and password superuser123");
            }

        };
    }

}
