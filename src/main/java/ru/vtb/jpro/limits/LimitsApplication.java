package ru.vtb.jpro.limits;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Сервис управления лимитами клиентов.
 * API доступен по адресу: /limit/swagger-ui/index.html
 */

@SpringBootApplication
@EnableScheduling
@ConfigurationProperties
public class LimitsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LimitsApplication.class, args);
    }
}
