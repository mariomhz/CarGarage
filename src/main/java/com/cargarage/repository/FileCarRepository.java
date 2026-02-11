package com.cargarage.repository;

import com.cargarage.model.Car;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * File-based implementation of ICarRepository.
 * Persists car data in a JSON file for durability across application restarts.
 */

public class FileCarRepository implements ICarRepository{
    private final String FILE_PATH = "data/cars.json";
    private Map<String, Car> cars;
    private int nextId = 1;
    private final Gson gson;

    public FileCarRepository() {
        this.gson = new Gson();
        this.cars = new HashMap<>();
        loadFromFile();
    }

    private void loadFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                saveToFile();
                System.out.println("nnew data file: " + FILE_PATH);
                return;
            }

            String json = new String(Files.readAllBytes(Paths.get(FILE_PATH)));

            if (json.trim().isEmpty()) {
                this.cars = new HashMap<>();
                System.out.println("initialized empty repository from file");
                return;
            }

            Type type = new TypeToken<HashMap<String, Car>>(){}.getType();
            this.cars = gson.fromJson(json, type);

            if (this.cars == null) {
                this.cars = new HashMap<>();
            }

            updateNextId();

            System.out.println("loaded " + cars.size() + " cars from file");

        } catch (IOException e) {
            System.err.println("error loading from file: " + e.getMessage());
            this.cars = new HashMap<>();
        }
    }

    private void saveToFile() {
        try {
            String json = gson.toJson(cars);
            Files.write(Paths.get(FILE_PATH), json.getBytes());
            System.out.println("data saved to file (" + cars.size() + " cars)");
        } catch (IOException e) {
            System.err.println("error saving to file: " + e.getMessage());
        }
    }

    private void updateNextId() {
        int maxId = cars.keySet().stream()
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);
        this.nextId = maxId + 1;
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
        saveToFile();
        return car;
    }

    @Override
    public void deleteById(String id) {
        cars.remove(id);
        saveToFile();
    }

    @Override
    public boolean existsByPlate(String plate) {
        return cars.values().stream()
                .anyMatch(car -> car.getPlate().equals(plate));
    }
}
