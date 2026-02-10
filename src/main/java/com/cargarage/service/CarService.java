package com.cargarage.service;

import com.cargarage.model.Car;
import com.cargarage.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing Car entities. Provides business logic for CRUD operations and plate uniqueness validation.
 */

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> getCarById(String id) {
        return carRepository.findById(id);
    }

    public Car saveCar(Car car) {
        if (carRepository.existsByPlate(car.getPlate())) {
            throw new IllegalArgumentException("Car with plate " + car.getPlate() + " already exists.");
        }
        if (car.getId() == null || car.getId().isEmpty()) {
            car.setId(carRepository.generateId());
        }
        return carRepository.save(car);
    }

    public Car updateCar(String id, Car updatedCar) {
          Car existingCar = carRepository.findById(id)
              .orElseThrow(() -> new IllegalArgumentException("Car not found"));

          if (!existingCar.getPlate().equals(updatedCar.getPlate())
              && carRepository.existsByPlate(updatedCar.getPlate())) {
              throw new IllegalArgumentException("Plate already exists");
          }

          updatedCar.setId(id);
          return carRepository.save(updatedCar);
      }

    public void deleteCar(String id) {
        if (!carRepository.findById(id).isPresent()) {
          throw new IllegalArgumentException("Car not found");
        }
        carRepository.deleteById(id);
    }
}