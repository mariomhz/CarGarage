package com.cargarage.service;

import com.cargarage.config.BeanFactory;
import com.cargarage.model.Car;
import com.cargarage.repository.ICarRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing operations.
 * Implements business logic for handling Car entities, including validation and interaction with the repository.
 * Uses BeanFactory to obtain the ICarRepository instance, allowing for flexibility in repository implementation.
 */

@Service
public class CarService {

    @Autowired
    private BeanFactory beanFactory;

    private ICarRepository carRepository;

    @PostConstruct
    public void init() {
        this.carRepository = beanFactory.getBean(ICarRepository.class);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> getCarById(String id) {
        return carRepository.findById(id);
    }

    public Car saveCar(Car car) {
        if (car.getPlate() == null || car.getPlate().trim().isEmpty()) {
            throw new IllegalArgumentException("License plate cannot be null or empty.");
        }
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

          if (updatedCar.getPlate() == null || updatedCar.getPlate().trim().isEmpty()) {
              throw new IllegalArgumentException("License plate cannot be null or empty.");
          }

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