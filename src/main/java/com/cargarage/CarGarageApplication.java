package com.cargarage;

import com.cargarage.controller.CarController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarGarageApplication implements CommandLineRunner {

    private final CarController controller;

    public CarGarageApplication(CarController controller) {
        this.controller = controller;
    }

    public static void main(String[] args) {
        SpringApplication.run(CarGarageApplication.class, args);
    }

    @Override
    public void run(String... args) {
        controller.start();
    }
}
