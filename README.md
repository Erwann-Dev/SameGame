# SameGame - Java Project

## Project Description

This project is a Java implementation of the tile-based puzzle game **SameGame**.

The goal of the game is to remove groups of adjacent tiles with the same color. The project focuses on clean object-oriented design, Java programming principles, and the correct use of design patterns.

This implementation is designed to meet the **grade 3 requirements** of the Advanced Object-Oriented Programming project.

## Grade 3 Requirements

To achieve grade 3, the project must include the following elements:

### 1. Functional SameGame Implementation

The game must be playable and include the core rules of SameGame:

- A grid of colored tiles
- Selection of groups of adjacent tiles with the same color
- Removal of valid groups
- Gravity applied after tile removal
- Detection of end-of-game conditions
- Score calculation

### 2. Use of MVC Architecture

The project follows the **Model-View-Controller (MVC)** design pattern.

MVC separates the application into three main parts:

- **Model**: contains the game logic and state
- **View**: displays the game state to the user
- **Controller**: handles user input and updates the model

This separation makes the code easier to understand, maintain, and extend.

### 3. Model

The model is responsible for:

- Storing the game board
- Managing the current game state
- Handling the difficulty level, such as the number of colors
- Processing game actions
- Checking win, lose, or end-game conditions
- Notifying views when the game state changes

### 4. Views

The project must provide at least **two different views** of the game state.

Example views:

- A graphical view using Java Swing
- A console/text view for debugging or logging

The views should be registered and de-registered using an observer-like pattern.

### 5. Controller

The controller handles user input and sends commands to the model.

At least one input method must be implemented, for example:

- Mouse input
- Keyboard input

The design should allow other input methods to be added later.

### 6. Observer Pattern

The project should use an observer-like pattern so that views are updated automatically when the model changes.

When the game state changes:

1. The model updates its internal state
2. The model notifies registered observers
3. Each view refreshes its display

### 7. Re-pluggable Input Design

The input system should be designed so that different control methods can be added or replaced easily.

For example:

- Mouse controller
- Keyboard controller
- Network controller

Only one default input method is required for grade 3.

## Project Structure

```text
src/
├── model/
│   ├── GameModel.java
│   ├── Board.java
│   ├── Tile.java
│   └── GameState.java
│
├── view/
│   ├── GameView.java
│   ├── SwingView.java
│   └── ConsoleView.java
│
├── controller/
│   ├── GameController.java
│   └── MouseController.java
│
├── observer/
│   ├── GameObserver.java
│   └── GameSubject.java
│
└── Main.java
