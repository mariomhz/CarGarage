# CarGarage

STEP 1 – In-Memory CRUD

## Problem

Develop a small command-line interface (CLI) application that manages a car garage. The application must allow CRUD operations on a set of cars stored in memory.

## Minimum Data Model (Car)

- `id` (String)
- `brand` (Brand)
- `model` (Model)
- `year` (Year)
- `plate` (License Plate)

## Required Functionalities

The menu must allow:

- List of cars
- Car detail by ID
- Add car
- Update car
- Delete car
- Exit

## Restrictions

- In-memory data (free structure)
- Minimum validations (unique ID, license plate cannot be null)
- Command-line user interface (UI)

STEP 2 – Performance Optimization

## Problem

The number of cars is growing and searching by ID has become too slow.

## Objective

Improve search performance without changing the user interface.

## Restrictions

- CLI behavior must remain identical
- No functional regressions
- Freedom to change data structure or internal design

STEP 3 – Factory Pattern & Persistence

## Problem

One client wants simple persistence, another wants faster persistence, and a third prefers a database. All options must be selectable without recompiling.

## Objective

Implement a Factory pattern that:

- Reads from application.properties which implementation to use
- Instantiates the correct class via Reflection
- Stores created objects (beans) in a cache (one per type)

## Restrictions

- No if/else scattered throughout the code to choose persistence
- The UI must not know the concrete type of DAO (Data Access Object)