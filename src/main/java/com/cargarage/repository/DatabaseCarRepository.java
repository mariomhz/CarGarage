package com.cargarage.repository;

import com.cargarage.model.Car;

import java.util.*;

/**
 * Simulates a database repository for cars.
 */

public class DatabaseCarRepository implements ICarRepository{
    private final Map<String, Car> cars = new HashMap<>();
    private int nextId = 1;

    public DatabaseCarRepository() {
        System.out.println("Connecting to database...");
        System.out.println("Database connection established.");
    }

    @Override
    public String generateId() {
        return String.valueOf(nextId++);
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(cars.values());
    }

    @Override
    public Optional<Car> findById(String id) {
        return Optional.ofNullable(cars.get(id));
    }

    @Override
    public Car save(Car car) {
        cars.put(car.getId(), car);
        return car;
    }

    @Override
    public void deleteById(String id) {
        cars.remove(id);
    }

    @Override
    public boolean existsByPlate(String plate) {
        return cars.values().stream()
                .anyMatch(car -> car.getPlate().equals(plate));
    }
}
