package com.cargarage.repository;

import com.cargarage.model.Car;
import org.springframework.stereotype.Repository;

import java.util.*;

/** 
 * In-memory repository for managing Car entities. Provides basic CRUD operations and plate uniqueness check.
 */

@Repository
public class CarRepository {
    private final Map<String, Car> cars = new HashMap<>();
    private int nextId = 1;

    public String generateId() {
        return String.valueOf(nextId++);
    }

    public List<Car> findAll() {
        return new ArrayList<>(cars.values());
    }

    public Optional<Car> findById(String id) {
        return Optional.ofNullable(cars.get(id));
    }

    public Car save(Car car) {
        cars.put(car.getId(), car);
        return car;
    }

    public void deleteById(String id) {
        cars.remove(id);
    }

    public boolean existsByPlate(String plate) {
        return cars.values().stream().anyMatch(car -> car.getPlate().equals(plate));
    }
}