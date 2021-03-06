package com.fluna245827;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
public class ParkingLibServicesApplication {

  public static void main(String[] args) {
    SpringApplication.run(ParkingLibServicesApplication.class, args);
  }

  @Bean
  public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption, @Value("${application-version}") String appVersion) {
    return new OpenAPI().info(new Info().title("ParkingLib Services API").version(appVersion).description(appDesciption).termsOfService("http://swagger.io/terms/")
        .license(new License().name("Apache License Version 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")).contact(new Contact().name("Felipe Luna").email("lune245827@gmail.com")));
  }
}
