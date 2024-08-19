package ru.vtb.jpro.limits;


import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


/**
 * Сервис управления лимитами клиентов.
 * API доступен по адресу: /limit/swagger-ui/index.html
 */

@SpringBootApplication
@EnableScheduling
@EnableWebSecurity
public class LimitsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LimitsApplication.class, args);
    }

    @Bean
    SecurityFilterChain filterChainError(HttpSecurity http) throws Exception {
        return http
                   .authorizeHttpRequests(authorize -> authorize
                                                           .requestMatchers("/v1/admin/**").authenticated()
                                                           .requestMatchers("/v1/**").permitAll()
                   )
                   .formLogin(withDefaults())
                   .httpBasic(withDefaults())
                   .build();
    }

    @Bean
    InMemoryUserDetailsManager usersInMemory() {
        return new InMemoryUserDetailsManager(
            new User("admin", "{noop}adminPassword", List.of(() -> "ROLE_ADMIN")
        ));
    }

}
