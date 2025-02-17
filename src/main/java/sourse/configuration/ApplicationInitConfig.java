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
import sourse.entity.User;
import sourse.enums.EnumType;
import sourse.repository.UserRepository;

import java.util.HashSet;


@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
           if( userRepository.findByEmail("admin3@admin.com").isEmpty()){
               var roles = new HashSet<String>();
               roles.add(EnumType.UserRole.SUPERUSER.name());
               User user = User.builder().email("admin3@admin.com").password(passwordEncoder.encode("admin1233")).roles(roles).build();
               userRepository.save(user);
               System.out.println("created admin");
                log.warn("default create 1 user with email" + "admin@admin.com" + "and password" +"admin");
            }else {
               log.warn("user already exist");

           }        };
    }

}
