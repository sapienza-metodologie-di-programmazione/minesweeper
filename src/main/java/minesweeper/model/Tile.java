package minesweeper.model;

import static minesweeper.model.Tile.Visibility.*;

import java.util.Observable;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class Tile extends Observable {

    public enum Visibility {
        Hidden, Flagged, Revealed
    }

    public final int x, y;
    public final boolean isMine;
    private Visibility visibility = Hidden;
    Optional<Integer> adjacentMines = Optional.empty();

    Tile(int x, int y, boolean isMine) {
        this.x = x;
        this.y = y;
        this.isMine = isMine;
    }

    public Visibility visibility() {
        return visibility;
    }

    public Optional<Integer> adjacentMines() {
        return adjacentMines;
    }

    public void reveal() {
        if (visibility != Hidden)
            return;

        setChanged();
        notifyObservers(visibility = Revealed);
    }

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

// public enum Kind {
// Mine,
// Empty
// }

// public final Kind kind;
// Tile(int x, int y, Kind kind) {
// this.kind = kind;
