# Architecture

## Overview

SameGame follows the **Model-View-Controller (MVC)** pattern combined with the **Observer** pattern. These two patterns work together to enforce a strict separation between game logic, display, and input:

- The **Model** holds all game state and knows nothing about UI or input.
- The **Views** display state but never modify it.
- The **Controller** receives input and translates it into model calls.
- The **Observer** mechanism lets the model push updates to views without depending on them.

```
User input
    │
    ▼
MouseController          (controller package)
    │  selectTile / hoverTile
    ▼
GameModel                (model package)
    │  notifyObservers()
    ├──────────────────────────┐
    ▼                          ▼
SwingView               ConsoleView       (view package)
(Swing GUI)             (stdout debug)
```

---

## Packages

### `observer`

Defines the two interfaces that make up the observer pattern.

| Class | Role |
|---|---|
| `GameObserver` | Any class that wants to receive model updates implements this. Has one method: `update(GameModel)`. |
| `GameSubject` | Any class that broadcasts state changes implements this. Provides `addObserver`, `removeObserver`, and `notifyObservers`. |

`GameModel` implements `GameSubject`. `SwingView` and `ConsoleView` implement `GameObserver` (via `GameView`).

---

### `model`

Contains all game state and rules. No imports from `view` or `controller`.

| Class | Role |
|---|---|
| `Tile` | A single cell. Holds a color integer (0 = empty, 1–N = game colors). Mutable so the board can clear cells in-place. |
| `GameState` | Enum with three values: `PLAYING`, `WIN`, `LOSE`. |
| `Board` | The 2-D grid of `Tile` objects. Implements flood-fill group detection, gravity, and column collapse. |
| `GameModel` | Owns the `Board`, score, `GameState`, and observer list. Entry point for all game actions. |

#### Board mechanics

**Flood-fill** (`Board.findGroup`): starting from a seed tile, a recursive depth-first search visits all 4-directionally adjacent tiles with the same color. Returns a list of `int[]{row, col}` positions.

**Gravity** (`Board.applyGravity`): after removal, each column is compacted from the bottom up — non-empty tiles slide down to fill gaps.

**Column collapse** (`Board.collapseColumns`): after gravity, any column whose bottom cell is empty is erased and all columns to its right shift left by one.

#### Scoring

When a group of `n` tiles is removed, the score increases by `(n - 2)²`. Removing 2 tiles scores 0; larger groups are rewarded quadratically.

#### End conditions

After every removal, `GameModel` checks:
- If the board is completely empty → `WIN`.
- If no group of at least 2 tiles exists anywhere → `LOSE`.

---

### `view`

Passive renderers. Both views are registered as observers on the model and redraw on every `update(GameModel)` call.

| Class | Role |
|---|---|
| `GameView` | Interface that extends `GameObserver` and adds `display()` and `close()` lifecycle methods. |
| `SwingView` | Swing `JFrame` with a custom `BoardPanel` (inner class) for drawing, plus a status bar. Exposes `getBoardComponent()` so the controller can attach listeners without a circular import. |
| `ConsoleView` | Prints the board as ASCII characters to stdout. Used for debugging alongside `SwingView`. |

**Highlight preview**: `SwingView` reads `GameModel.getHighlightedGroup()` during each paint. Tiles in that list are drawn brighter and outlined in white so the player can see what will be removed before clicking.

---

### `controller`

Handles input and translates raw events into model method calls.

| Class | Role |
|---|---|
| `GameController` | Interface with `attach()` and `detach()` lifecycle methods. New input methods (keyboard, network, etc.) implement this and plug in via `Main`. |
| `MouseController` | Extends `MouseAdapter` and implements `GameController`. Converts pixel coordinates to grid positions using `SwingView.TILE_SIZE`, then calls `selectTile` (click) or `hoverTile` (move) on the model. |

---

### `Main`

Wires all layers together and starts the application.

1. Creates `GameModel` with 3 colors.
2. Registers `ConsoleView` before the Swing EDT starts (stdout is thread-safe).
3. On the Swing EDT: creates `SwingView`, registers it, creates and attaches `MouseController`, calls `display()`, then triggers the first render via `notifyObservers()`.

---

## Data flow for a single click

```
1. User clicks on the window
2. Swing calls MouseController.mouseClicked(event)
3. Controller converts pixels → (row, col) and calls GameModel.selectTile(row, col)
4. GameModel runs flood-fill on Board to find the group
5. If group size >= 2:
     a. Score += (n-2)²
     b. Board.removeTiles() → clears tiles → applyGravity() → collapseColumns()
     c. GameModel checks WIN / LOSE condition, updates state
6. GameModel.notifyObservers() is called
7. SwingView.update(model) → scoreLabel, stateLabel updated → boardPanel.repaint()
8. ConsoleView.update(model) → board printed to stdout
```

---

## How to extend

**Add a new view**: implement `GameView`, call `model.addObserver(newView)` in `Main`. No other changes needed.

**Add a new controller** (e.g. keyboard): implement `GameController`, call `model.selectTile` or any other model method in response to events, call `attach()` in `Main`. No changes to model or views.

**Change board size or color count**: modify the constants in `GameModel` (`ROWS`, `COLS`) or pass a different `numColors` argument. No other changes needed.
