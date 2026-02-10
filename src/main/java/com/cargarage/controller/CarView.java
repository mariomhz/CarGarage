package com.cargarage.controller;

import com.cargarage.model.Car;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

/**
 * View class for displaying car information and interacting with the user.
 * It provides methods to show menus, display car details, and get user input.
 */

@Component
public class CarView {
    private final Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        System.out.println("\n---Car Garage Management---");
        System.out.println("1. List all cars");
        System.out.println("2. View car details");
        System.out.println("3. Add new car");
        System.out.println("4. Update car");
        System.out.println("5. Delete car");
        System.out.println("6. Exit");
        System.out.print("Choose: ");
    }

    public String getInput(String prompt) {
        if (!prompt.isEmpty()) {
            System.out.print(prompt + ": ");
        }
        return scanner.nextLine();
    }

    public void showCars(List<Car> cars) {
        if (cars.isEmpty()) {
            System.out.println("No cars in garage.");
            return;
        }
        System.out.println("\n---All Cars---");
        for (Car car : cars) {
            System.out.println(car.getId() + " | " + car.getBrand() + " " +
                             car.getModel() + " (" + car.getYear() + ") - " + car.getPlate());
        }
    }

    public void showCar(Car car) {
        if (car == null) {
            System.out.println("Car not found.");
            return;
        }
        System.out.println("\n---Car Details---");
        System.out.println("ID: " + car.getId());
        System.out.println("Brand: " + car.getBrand());
        System.out.println("Model: " + car.getModel());
        System.out.println("Year: " + car.getYear());
        System.out.println("Plate: " + car.getPlate());
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void close() {
        scanner.close();
    }
}
