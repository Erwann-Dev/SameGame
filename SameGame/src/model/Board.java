package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the SameGame grid and implements all board-manipulation rules.
 *
 * The grid is a 2-D array of {@link Tile} objects addressed by [row][col],
 * where row 0 is the top and rows-1 is the bottom.
 *
 * After tiles are removed, two physics steps are applied in order:
 * 1. Gravity: within each column, non-empty tiles slide down to fill gaps.
 * 2. Column collapse: completely empty columns are removed and all remaining
 *    columns shift left.
 */
public class Board {
//test
    private final int rows;
    private final int cols;
    private Tile[][] grid;

    /**
     * Creates a new board filled with randomly colored tiles.
     *
     * @param rows      number of rows (height of the grid)
     * @param cols      number of columns (width of the grid)
     * @param numColors number of distinct tile colors to use (at least 1);
     *                  a larger value makes the board harder to clear
     */
    public Board(int rows, int cols, int numColors) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Tile[rows][cols];
        randomize(numColors);
    }

    /**
     * Fills every cell with a random color in the range [1, numColors].
     *
     * @param numColors number of distinct colors available
     */
    private void randomize(int numColors) {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = new Tile((int) (Math.random() * numColors) + 1);
    }

    /**
     * Returns the tile at the specified position.
     *
     * @param row row index (0 = top)
     * @param col column index (0 = left)
     * @return the {@link Tile} at [row][col]
     */
    public Tile getTile(int row, int col) { return grid[row][col]; }

    /**
     * Returns the number of rows in the grid.
     *
     * @return row count
     */
    public int getRows() { return rows; }

    /**
     * Returns the number of columns in the grid.
     *
     * @return column count
     */
    public int getCols() { return cols; }

    /**
     * Finds all tiles that form a connected group with the tile at (row, col)
     * using a 4-directional flood-fill.
     *
     * Two tiles are connected if they share an edge (not diagonal) and have
     * the same color. Returns an empty list if the seed tile is empty.
     *
     * @param row row index of the seed tile
     * @param col column index of the seed tile
     * @return list of int[]{row, col} pairs belonging to the group;
     *         empty if the seed is empty or has no same-color neighbors
     */
    public List<int[]> findGroup(int row, int col) {
        if (grid[row][col].isEmpty()) return new ArrayList<>();
        int color = grid[row][col].getColor();
        List<int[]> group = new ArrayList<>();
        boolean[][] visited = new boolean[rows][cols];
        floodFill(row, col, color, visited, group);
        return group;
    }

    /**
     * Recursive flood-fill helper. Visits each reachable cell of the given
     * color and appends it to {@code group}.
     *
     * @param r       current row
     * @param c       current column
     * @param color   target color to match
     * @param visited tracks already-visited cells to prevent revisiting
     * @param group   accumulator for matching cell positions
     */
    private void floodFill(int r, int c, int color, boolean[][] visited, List<int[]> group) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) return;
        if (visited[r][c] || grid[r][c].getColor() != color) return;
        visited[r][c] = true;
        group.add(new int[]{r, c});
        floodFill(r - 1, c, color, visited, group);
        floodFill(r + 1, c, color, visited, group);
        floodFill(r, c - 1, color, visited, group);
        floodFill(r, c + 1, color, visited, group);
    }

    /**
     * Clears all tiles in {@code group}, then applies gravity and column
     * collapse to produce the board's next stable state.
     *
     * @param group list of int[]{row, col} positions to remove;
     *              typically the result of a {@link #findGroup} call
     */
    public void removeTiles(List<int[]> group) {
        for (int[] pos : group)
            grid[pos[0]][pos[1]].setColor(0);
        applyGravity();
        collapseColumns();
    }

    /**
     * Applies gravity: within every column, non-empty tiles fall downward to
     * fill gaps, preserving their relative order.
     */
    private void applyGravity() {
        for (int c = 0; c < cols; c++) {
            int write = rows - 1;
            for (int r = rows - 1; r >= 0; r--) {
                if (!grid[r][c].isEmpty()) {
                    grid[write][c] = grid[r][c];
                    if (write != r) grid[r][c] = new Tile(0);
                    write--;
                }
            }
            while (write >= 0) {
                grid[write][c] = new Tile(0);
                write--;
            }
        }
    }

    /**
     * Collapses empty columns: any column whose bottom cell is empty is
     * removed, and all columns to its right shift one position left.
     */
    private void collapseColumns() {
        int write = 0;
        for (int c = 0; c < cols; c++) {
            if (!grid[rows - 1][c].isEmpty()) {
                if (write != c) {
                    for (int r = 0; r < rows; r++) {
                        grid[r][write] = grid[r][c];
                        grid[r][c] = new Tile(0);
                    }
                }
                write++;
            }
        }
    }

    /**
     * Checks whether at least one valid move remains on the board.
     * A valid move requires a group of at least 2 adjacent same-color tiles.
     *
     * @return true if a removable group exists, false otherwise
     */
    public boolean hasValidMoves() {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (!grid[r][c].isEmpty() && findGroup(r, c).size() >= 2)
                    return true;
        return false;
    }

    /**
     * Returns true if every cell on the board is empty (win condition).
     *
     * @return true when the board is fully cleared
     */
    public boolean isEmpty() {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (!grid[r][c].isEmpty()) return false;
        return true;
    }
}
