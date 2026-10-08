# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Compile (from project root)
javac -d out src/observer/*.java src/model/*.java src/view/*.java src/controller/*.java src/Main.java

# Run
java -cp out Main
```

No build tool — plain `javac`. Order matters: observer → model → view → controller → Main.

## Architecture

MVC pattern — strict separation:

- **`src/model/`** — game state only. `GameModel` owns `Board` (2D `Tile[][]`), score, `GameState` enum. No UI references.
- **`src/view/`** — passive renderers. `SwingView` (Swing GUI) and `ConsoleView` (text/debug). Both implement `GameView` and register as observers on the model.
- **`src/controller/`** — input only. `MouseController` translates Swing events → model method calls. New input methods plug in here without touching model or view.
- **`src/observer/`** — `GameSubject` (register/notify) implemented by model; `GameObserver` implemented by views.
- **`src/Main.java`** — wires model → controller → views, then starts game loop.

### Key data flow

1. User clicks → `MouseController` → `GameModel.selectTile(x, y)`
2. Model validates group (flood-fill adjacency), removes tiles, applies gravity, updates score, checks end condition
3. Model calls `notifyObservers()`
4. Each registered view pulls state and redraws

### SameGame rules to implement

- Valid group = ≥2 adjacent same-color tiles (4-directional)
- Score = `(n-2)²` per group removal where `n` = group size
- Gravity: tiles fall down after removal; empty columns collapse left
- End: no valid groups remain (lose if tiles left, win if board empty)
