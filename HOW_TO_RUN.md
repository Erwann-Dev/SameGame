# How to Run

## Requirements

- JDK 17 or later
- No build tool required (plain `javac`)

## Compile

Run from the project root. Order matters — `observer` and `model` must compile before `view` and `controller`.

```bash
javac -d out src/observer/*.java src/model/*.java src/view/*.java src/controller/*.java src/Main.java
```

## Run

```bash
java -cp out Main
```

## What to expect

- A Swing window opens with a 15×10 colored tile grid.
- The console prints the board state after every move (debug view).
- Hover over tiles to preview a group. Click to remove it.
- Score formula: **(n − 2)²** per removal, where n is the group size.
- Click **Restart** in the status bar to start a new game.

## End conditions

| Condition | Result |
|---|---|
| Board fully cleared | YOU WIN |
| Tiles remain, no valid group | GAME OVER |
