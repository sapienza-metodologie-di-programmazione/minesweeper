package minesweeper.model;

import static minesweeper.model.Tile.Visibility.*;

import java.util.Observable;
import java.util.Optional;

/**
 * The Tile class represents a single tile in the Minesweeper game.
 *
 * @author Cicio Ionut
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class Tile extends Observable {

    /**
     * The Visibility enum represents the visibility of a tile.
     *
     * @author Cicio Ionut
     * @version 1.0
     */
    public enum Visibility {
        Hidden, Flagged, Revealed
    }

    public final int x, y;
    public final boolean isMine;
    private Visibility visibility = Hidden;
    Optional<Integer> adjacentMines = Optional.empty();

    /**
     * Class constructor specifying the coordiantes and wether the tile is a mine.
     *
     * @param x      the x coordinate in the grid
     * @param y      the y coordinate in the grid
     * @param isMine whether the tile is a mine
     */
    Tile(int x, int y, boolean isMine) {
        this.x = x;
        this.y = y;
        this.isMine = isMine;
    }

    /**
     * Returns the visibility of the tile.
     *
     * @return the visibility of the tile
     */
    public Visibility visibility() {
        return visibility;
    }

    /**
     * Returns the number of adjacent mines.
     *
     * @return the number of adjacent mines
     */
    public Optional<Integer> adjacentMines() {
        return adjacentMines;
    }

    /**
     * Reveals the tile if it's hidden.
     */
    public void reveal() {
        if (visibility != Hidden)
            return;

        setChanged();
        notifyObservers(visibility = Revealed);
    }

    /**
     * Flags the tile if it's hidden or unflags it if it's flagged.
     */
    public void flag() {
        setChanged();
        notifyObservers(visibility = switch (visibility) {
            case Hidden -> Flagged;
            case Flagged -> Hidden;
            case Revealed -> {
                clearChanged();
                yield Revealed;
            }
        });
    }

}
