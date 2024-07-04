package minesweeper.model;

import static minesweeper.model.Tile.Visibility.Flagged;
import static minesweeper.model.Tile.Visibility.Hidden;
import static minesweeper.model.Tile.Visibility.Revealed;

import java.util.Observable;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class Tile extends Observable {

    public enum Kind {
        Mine,
        Empty
    }

    public enum Visibility {
        Hidden,
        Flagged,
        Revealed
    }

    public final int x, y;
    public final Kind kind;
    Visibility visibility = Visibility.Hidden;
    Optional<Integer> adjacentMines = Optional.empty();

    Tile(int x, int y, Kind kind) {
        this.x = x;
        this.y = y;
        this.kind = kind;
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

        visibility = Revealed;

        setChanged();
        notifyObservers(Revealed);
    }

    public void flag() {
        visibility = switch (visibility) {
            case Hidden -> {
                setChanged();
                yield Flagged;
            }
            case Flagged -> {
                setChanged();
                yield Hidden;
            }
            case Revealed -> Revealed;
        };

        notifyObservers(visibility);
    }

}
