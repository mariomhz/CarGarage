package com.cargarage.repository;

import com.cargarage.model.Car;

import java.util.List;
import java.util.Optional;

/**
 * Repository contract for Car entity.
 * Defines CRUD operations for managing Car entities with different persistence strategies.
 * Supports multiple storage implementations (memory, file, database).
 */

public interface ICarRepository {

    String generateId();

    List<Car> findAll();

    Optional<Car> findById(String id);

    Car save(Car car);

    void deleteById(String id);

    boolean existsByPlate(String plate);
}
