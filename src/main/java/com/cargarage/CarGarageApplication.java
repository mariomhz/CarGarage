package com.cargarage;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarGarageApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CarGarageApplication.class, args);
    }
}
