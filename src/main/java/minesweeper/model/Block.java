package minesweeper.model;

public class Block {
    public final int x, y;

    public enum Type {
        Mine,
        Safe
    }

    public enum State {
        Hidden,
        Revealed,
        Flagged
    }

    Type type;
    State state;

    Block(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.state = State.Hidden;
    }

    public State state() {
        return state;
    }

    public Type type() {
        return type;
    }
}
