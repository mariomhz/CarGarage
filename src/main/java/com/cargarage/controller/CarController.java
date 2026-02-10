package com.cargarage.controller;

import com.cargarage.model.Car;
import com.cargarage.service.CarService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Controller class for managing car-related operations.
 * It interacts with the CarService for business logic and CarView for user interface.
 */

@Component
public class CarController {
    private CarService service;
    private CarView view;

    public CarController(CarService service, CarView view) {
        this.service = service;
        this.view = view;
    }

    public void start() {
        boolean running = true;
        while (running) {
            view.showMenu();
            String choice = view.getInput("");
            try {
                switch (choice) {
                    case "1" -> listAllCars();
                    case "2" -> showCarDetail();
                    case "3" -> addNewCar();
                    case "4" -> updateCar();
                    case "5" -> deleteCar();
                    case "6" -> {
                        view.showMessage("Exiting...");
                        running = false;
                    }
                    default -> view.showMessage("Invalid option.");
                }
            } catch (Exception e) {
                view.showMessage("Error: " + e.getMessage());
            }
        }
        view.close();
    }

    private void listAllCars() {
        List<Car> cars = service.getAllCars();
        view.showCars(cars);
    }

    private void showCarDetail() {
        String id = view.getInput("Enter ID");
        Car car = service.getCarById(id).orElse(null);
        view.showCar(car);
    }

    private void addNewCar() {
        String brand = view.getInput("Enter brand");
        String model = view.getInput("Enter model");
        int year = Integer.parseInt(view.getInput("Enter year"));
        String plate = view.getInput("Enter plate");

        Car newCar = new Car(null, brand, model, year, plate);
        service.saveCar(newCar);
        view.showMessage("Car added.");
    }

    private void updateCar() {
        String id = view.getInput("Enter ID to update");
        Car existing = service.getCarById(id).orElse(null);

        if (existing != null) {
            String brand = view.getInput("Enter new brand");
            String model = view.getInput("Enter new model");
            int year = Integer.parseInt(view.getInput("Enter new year"));
            String plate = view.getInput("Enter new plate");

            Car updated = new Car(id, brand, model, year, plate);
            service.updateCar(id, updated);
            view.showMessage("Car updated.");
        } else {
            view.showMessage("Car not found.");
        }
    }

    private void deleteCar() {
        String id = view.getInput("Enter ID to delete");
        service.deleteCar(id);
        view.showMessage("Car deleted.");
    }
}
