package com.core.backend;

import com.core.backend.model.Role;
import com.core.backend.model.User;
import com.core.backend.repository.RoleRepository;
import com.core.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserRepository userRepository, RoleRepository roleRepository){
      return args -> {

        //  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //  System.out.println(passwordEncoder.encode("123456"));

        //  Role role1 = new Role();
        //  role1.setRoleName("ROLE_USER");
        //  roleRepository.save(role1);

        //  Role role2 = new Role();
        //  role2.setRoleName("ROLE_ADMIN");
        //  roleRepository.save(role2);

        //  User user1 = new User();
        //  user1.setRole(role1);
        //  user1.setEmail("user@gmail.com");
        //  user1.setEmailConfirmed(true);
        //  user1.setPasswordHash(passwordEncoder.encode("1234567"));
        //  userRepository.save(user1);

        //  User user2 = new User();
        //  user2.setRole(role2);
        //  user2.setEmail("admin@gmail.com");
        //  userRepository.save(user2);

      };
    }

}
