package com.cargarage.dao;

/**
 * Data Access Object (DAO) class for Car entity operations using JDBC.
 * This class provides methods to perform CRUD (Create, Read, Update, Delete)
 * operations on Car records in the database using JDBC (Java Database Connectivity).
 */

import com.cargarage.model.Car;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CarJdbcDao {
    private static final Logger log = Logger.getLogger(CarJdbcDao.class.getName());
    
    private static final String URL = "jdbc:h2:./data/garage";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final String DRIVER = "org.h2.Driver";

    private static final String INSERT_CAR = "INSERT INTO cars (id, brand, model, car_year, plate) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_CARS = "SELECT id, brand, model, car_year, plate FROM cars ORDER BY id";
    private static final String SELECT_CAR_BY_ID = "SELECT id, brand, model, car_year, plate FROM cars WHERE id = ?";
    private static final String UPDATE_CAR = "UPDATE cars SET brand = ?, model = ?, car_year = ?, plate = ? WHERE id = ?";
    private static final String DELETE_CAR = "DELETE FROM cars WHERE id = ?";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            log.severe("JDBC Driver not found: " + e.getMessage());
        }
    }

    public void create(Car car) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(INSERT_CAR)) {
            logSQL(INSERT_CAR, new Object[]{car.getId(), car.getBrand(), car.getModel(), car.getYear(), car.getPlate()});
            
            stmt.setString(1, car.getId());
            stmt.setString(2, car.getBrand());
            stmt.setString(3, car.getModel());
            stmt.setInt(4, car.getYear());
            stmt.setString(5, car.getPlate());
            stmt.executeUpdate();
            
            log.info("Car saved with ID: " + car.getId());
        } catch (SQLException e) {
            handleSQLException(e);
            throw e;
        }
    }

    public List<Car> readAll() throws SQLException {
        List<Car> cars = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_CARS)) {
            logSQL(SELECT_ALL_CARS, null);
            
            while (rs.next()) {
                cars.add(mapResultSetToCar(rs));
            }
            log.info("Retrieved " + cars.size() + " cars from database");
        } catch (SQLException e) {
            handleSQLException(e);
            throw e;
        }
        return cars;
    }

    public Car readById(String id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(SELECT_CAR_BY_ID)) {
            logSQL(SELECT_CAR_BY_ID, new Object[]{id});
            
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCar(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
            throw e;
        }
        return null;
    }

    public void update(Car car) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CAR)) {
            logSQL(UPDATE_CAR, new Object[]{car.getBrand(), car.getModel(), car.getYear(), car.getPlate(), car.getId()});
            
            stmt.setString(1, car.getBrand());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setString(4, car.getPlate());
            stmt.setString(5, car.getId());
            stmt.executeUpdate();
            
            log.info("Car updated with ID: " + car.getId());
        } catch (SQLException e) {
            handleSQLException(e);
            throw e;
        }
    }

    public void delete(String id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(DELETE_CAR)) {
            logSQL(DELETE_CAR, new Object[]{id});
            
            stmt.setString(1, id);
            stmt.executeUpdate();
            
            log.info("Car deleted with ID: " + id);
        } catch (SQLException e) {
            handleSQLException(e);
            throw e;
        }
    }

    private Car mapResultSetToCar(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setId(rs.getString("id"));
        car.setBrand(rs.getString("brand"));
        car.setModel(rs.getString("model"));
        car.setYear(rs.getInt("car_year"));  // Map car_year DB column to year Java field
        car.setPlate(rs.getString("plate"));
        return car;
    }

    private void logSQL(String sql, Object[] params) {
        StringBuilder sb = new StringBuilder();
        sb.append("[CarJdbcDao] Executing SQL: ").append(sql);
        if (params != null && params.length > 0) {
            sb.append("\n             | Params: [");
            for (int i = 0; i < params.length; i++) {
                sb.append(params[i]);
                if (i < params.length - 1) sb.append(", ");
            }
            sb.append("]");
        }
        log.info(sb.toString());
    }

    private void handleSQLException(SQLException e) {
        log.severe("SQL Exception occurred: " + e.getMessage());
        log.info("Attempting to reconnect...");
    }
}