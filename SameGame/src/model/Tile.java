package model;

/**
 * Represents a single cell on the SameGame board.
 *
 * A tile's color is stored as a non-negative integer:
 * 0 means the cell is empty (cleared or never filled);
 * values 1 through N correspond to distinct colors,
 * where N is the number of colors chosen at game start.
 *
 * Tiles are mutable so {@code Board} can clear them in-place during
 * gravity and column-collapse operations without allocating new objects.
 */
public class Tile {

    /** Color index. 0 = empty; 1+ = a game color. */
    private int color;

    /**
     * Creates a tile with the given color.
     *
     * @param color color index (0 for empty, 1+ for a game color)
     */
    public Tile(int color) {
        this.color = color;
    }

    /**
     * Returns the color index of this tile.
     *
     * @return color index; 0 if the tile is empty
     */
    public int getColor() { return color; }

    /**
     * Sets the color index of this tile.
     *
     * @param color new color index; pass 0 to mark the tile as empty
     */
    public void setColor(int color) { this.color = color; }

    /**
     * Returns true if this tile has been cleared (color == 0).
     *
     * @return true when empty, false otherwise
     */
    public boolean isEmpty() { return color == 0; }
}
