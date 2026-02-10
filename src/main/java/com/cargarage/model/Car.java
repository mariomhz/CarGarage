package com.cargarage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a car in the garage with a single identifier.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    private String id;
    private String brand;
    private String model;
    private int year;
    private String plate;
}
