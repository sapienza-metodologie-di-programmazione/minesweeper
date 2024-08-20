package minesweeper.model;

import static minesweeper.model.Tile.Kind.Empty;
import static minesweeper.model.Tile.Kind.Mine;
import static minesweeper.model.Tile.Visibility.Flagged;
import static minesweeper.model.Tile.Visibility.Revealed;

import java.time.Duration;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import minesweeper.model.Tile.Visibility;

@SuppressWarnings("deprecation")
public class Game extends Observable implements Observer {

    public enum Result {
        Loss,
        Victory,
        Terminated
    }

    Duration duration = Duration.ofSeconds(0);
    public final Tile[] tiles = new Tile[100];
    public final int mines;
    int flags = 0;

    public Game() {
        Random random = new Random();

        for (int y = 0; y < 10; y++)
            for (int x = 0; x < 10; x++)
                tiles[y * 10 + x] = new Tile(x, y, random.nextInt(100) >= 15 ? Empty : Mine);

        for (Tile tile : tiles) {
            tile.addObserver(this);

            int adjacentMines = (int) adjacent(tile.x, tile.y)
                    .filter(t -> t.kind == Mine)
                    .count();

            if (tile.kind == Empty && adjacentMines > 0)
                tile.adjacentMines = Optional.of(adjacentMines);
        }

        mines = (int) Stream.of(tiles)
                .filter(t -> t.kind == Mine)
                .count();
    }

    public int flags() {
        return flags;
    }

    public Duration time() {
        return duration;
    }

    public void updateTime() {
        duration = duration.plusSeconds(1);
        setChanged();
        notifyObservers(duration);
    }

    public void end() {
        setChanged();
        notifyObservers(Result.Terminated);
        deleteObservers();
    }

    Stream<Tile> adjacent(int x, int y) {
        return Stream.of(new Integer[][] {
                { -1, -1 },
                { -1, 0 },
                { -1, 1 },
                { 0, -1 },
                { 0, 1 },
                { 1, -1 },
                { 1, 0 },
                { 1, 1 }
        })
                .map(p -> new int[] { x + p[0], y + p[1] })
                .filter(p -> p[0] >= 0 && p[0] < 10 && p[1] >= 0 && p[1] < 10)
                .map(p -> tiles[p[1] * 10 + p[0]]);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Tile tile && arg instanceof Visibility visibility) {
            switch (visibility) {
                case Flagged -> flags++;
                case Hidden -> flags--;
                case Revealed -> {
                    if (tile.kind == Mine) {
                        setChanged();
                        notifyObservers(Result.Loss);
                        deleteObservers();
                        return;
                    }

                    if (tile.adjacentMines().isEmpty())
                        adjacent(tile.x, tile.y).forEach(Tile::reveal);

                    boolean allEmptyRevealed = Stream.of(tiles)
                            .allMatch(t -> t.visibility() == Revealed || t.kind == Mine);

                    boolean allMinesFlagged = Stream.of(tiles)
                            .allMatch(t -> (t.kind == Mine && t.visibility() == Flagged)
                                    || (t.kind == Empty && t.visibility() != Flagged));

                    if (allEmptyRevealed || allMinesFlagged) {
                        setChanged();
                        notifyObservers(Result.Victory);
                        deleteObservers();
                    }
                }
            }
        }
    }

}

// import static minesweeper.model.Tile.Visibility.Hidden;

// public enum Message {
// Timer
// }
// notifyObservers(Message.Timer);

// && arg instanceof Visibility visibility

// switch (o) {
// case Tile tile -> {
// switch (arg) {
// case Tile.Visibility visibility -> {
// switch (visibility) {
// case Flagged -> flags++;
// case Hidden -> flags--;
// case Revealed -> {
// if (tile.kind == Mine) {
// setChanged();
// notifyObservers(Result.Loss);
// deleteObservers();
// return;
// }
//
// if (tile.adjacentMines().isEmpty())
// adjacent(tile.x, tile.y).forEach(Tile::reveal);
//
// boolean allEmptyRevealed = Stream.of(tiles)
// .allMatch(t -> t.visibility() == Revealed || t.kind == Mine);
//
// boolean allMinesFlagged = Stream.of(tiles)
// .allMatch(t -> (t.kind == Mine && t.visibility() == Flagged)
// || (t.kind == Empty && t.visibility() != Flagged));
//
// if (allEmptyRevealed || allMinesFlagged) {
// setChanged();
// notifyObservers(Result.Victory);
// deleteObservers();
// }
// }
// }
// }
// default -> {
// }
// }
// }
// default -> {
// }
// }

// return Stream.of(new Integer[][] {
// { x - 1, y - 1 },
// { x - 1, y },
// { x - 1, y + 1 },
// { x, y - 1 },
// { x, y + 1 },
// { x + 1, y - 1 },
// { x + 1, y },
// { x + 1, y + 1 }
// })
// .filter(p -> p[0] >= 0 && p[0] < 10 && p[1] >= 0 && p[1] < 10)
// .map(p -> tiles[p[1] * 10 + p[0]]);
