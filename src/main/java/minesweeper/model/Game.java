package minesweeper.model;

import static minesweeper.model.Tile.Visibility.*;
import static minesweeper.model.Game.Result.*;

import java.time.Duration;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import minesweeper.model.Tile.Visibility;

/**
 * The Game class represents a Minesweeper game.
 *
 * @author Cicio Ionut
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class Game extends Observable implements Observer {

    /**
     * The Result enum represents the possible results of a game.
     *
     * @author Cicio Ionut
     * @version 1.0
     */
    public enum Result {
        Loss, Victory, Terminated
    }

    private int flags = 0;
    public final int mines;
    public final Tile[] tiles = new Tile[100];
    private Duration duration = Duration.ofSeconds(0);

    /**
     * Class constructor.
     */
    public Game() {
        Random random = new Random();

        for (int y = 0; y < 10; y++)
            for (int x = 0; x < 10; x++)
                tiles[y * 10 + x] = new Tile(x, y, random.nextInt(100) <= 15);

        for (Tile tile : tiles) {
            tile.addObserver(this);

            if (!tile.isMine) {
                int adjacentMines = (int) adjacent(tile).filter(t -> t.isMine).count();

                if (adjacentMines > 0)
                    tile.adjacentMines = Optional.of(adjacentMines);
            }
        }

        mines = (int) Stream.of(tiles).filter(t -> t.isMine).count();
    }

    private Stream<Tile> adjacent(Tile tile) {
        return Stream.of(new Integer[][] {
                { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }
        })
                .map(p -> new int[] { tile.x + p[0], tile.y + p[1] })
                .filter(p -> p[0] >= 0 && p[0] < 10 && p[1] >= 0 && p[1] < 10)
                .map(p -> tiles[p[1] * 10 + p[0]]);
    }

    /**
     * Returns the number of flags placed on the tiles.
     *
     * @return the number of flags placed on the tiles
     */
    public int flags() {
        return flags;
    }

    /**
     * Returns the duration of the game.
     *
     * @return the duration of the game
     */
    public Duration duration() {
        return duration;
    }

    /**
     * Notifies all listeners that the game has started (in order to reset).
     */
    public void start() {
        setChanged();
        notifyObservers(this);
    }

    /**
     * Updates the duration of the game.
     */
    public void update() {
        setChanged();
        notifyObservers(duration = duration.plusSeconds(1));
    }

    /**
     * Notifies all listeners that the game has ended.
     */
    public void end() {
        setChanged();
        notifyObservers(Terminated);
        deleteObservers();
    }

    /**
     * Updates when notified by a tile.
     *
     * @param o   the tile
     * @param arg the visibility of the tile
     */
    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof Tile tile && arg instanceof Visibility visibility))
            return;

        setChanged();

        switch (visibility) {
            case Flagged -> flags++;
            case Hidden -> flags--;
            case Revealed -> {
                if (tile.isMine) {
                    notifyObservers(Loss);
                    deleteObservers();
                    return;
                }

                if (tile.adjacentMines().isEmpty())
                    adjacent(tile).forEach(Tile::reveal);

                boolean allEmptyRevealed = Stream.of(tiles)
                        .allMatch(t -> t.isMine || t.visibility() == Revealed);

                boolean allMinesFlagged = Stream.of(tiles)
                        .allMatch(t -> !(t.isMine ^ t.visibility() == Flagged));

                if (allEmptyRevealed || allMinesFlagged) {
                    notifyObservers(Victory);
                    deleteObservers();
                }
            }
        }

        notifyObservers(tile);
    }

}
