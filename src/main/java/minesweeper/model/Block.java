package minesweeper.model;

import java.io.Serializable;

public class Block implements Serializable {

    public enum Type {
        Mine,
        Safe
    }

    public enum Visibility {
        Hidden,
        Revealed,
        Flagged
    }

    public final int x, y;
    public final Type type;
    Visibility visibility = Visibility.Hidden;

    Block(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Visibility visibility() {
        return visibility;
    }
}
