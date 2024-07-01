package minesweeper.model;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static minesweeper.model.Block.Visibility.*;
import static minesweeper.model.Block.Type.*;

@SuppressWarnings("deprecation")
public class Game extends Observable implements Serializable {

    public enum Result {
        Loss,
        Victory,
        Terminated
    }

    public enum Message {
        Start,
        Update,
        Timer
    }

    Block[] blocks = new Block[100];
    public final int mines;
    Optional<Result> result = Optional.empty();

    int time = 0;
    ScheduledFuture<?> timer;

    Game(Observer... observers) {

        Random random = new Random();
        for (int y = 0; y < 10; y++)
            for (int x = 0; x < 10; x++)
                blocks[y * 10 + x] = new Block(x, y, random.nextInt(100) >= 15 ? Safe : Mine);

        mines = (int) Stream.of(blocks)
                .filter(block -> block.type == Mine)
                .count();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        timer = scheduler.scheduleAtFixedRate(() -> {
            time++;
            setChanged();
            notifyObservers(Message.Timer);
        }, 1, 1, TimeUnit.SECONDS);

        Stream.of(observers).forEach(this::addObserver);
        setChanged();
        notifyObservers(Message.Start);
    }

    void end(Result result) {
        if (this.result.isPresent())
            return;

        timer.cancel(false);
        setChanged();
        notifyObservers((this.result = Optional.of(result)));
        deleteObservers();
    }

    public void terminate() {
        end(Result.Terminated);
    }

    Stream<Block> neighbours(int x, int y) {
        return List.of(
                new Integer[] { x - 1, y - 1 },
                new Integer[] { x, y - 1 },
                new Integer[] { x + 1, y - 1 },
                new Integer[] { x - 1, y },
                new Integer[] { x + 1, y },
                new Integer[] { x - 1, y + 1 },
                new Integer[] { x, y + 1 },
                new Integer[] { x + 1, y + 1 } //
        )
                .stream()
                .filter(p -> p[0] >= 0 && p[0] < 10 && p[1] >= 0 && p[1] < 10)
                .map(p -> blocks[p[0] * 10 + p[1]]);
    }

    public int neighbourMines(int x, int y) {
        return (int) neighbours(x, y)
                .filter(block -> block.type == Mine)
                .count();
    }

    public void revealBlock(int x, int y) {
        if (result.isPresent())
            return;

        if (x < 0 || x > 9 || y < 0 || y > 9)
            return;

        Block block = blocks[y * 10 + x];

        if (block.visibility != Hidden)
            return;

        block.visibility = Revealed;

        if (block.type == Mine) {
            end(Result.Loss);
            return;
        }

        if (neighbourMines(x, y) == 0)
            neighbours(x, y).forEach(b -> revealBlock(b.x, b.y));

        boolean safeRevealed = Stream.of(this.blocks)
                .allMatch(b -> b.visibility == Revealed || b.type == Mine);

        boolean minesFlagged = Stream.of(this.blocks)
                .allMatch(b -> (b.type == Mine && b.visibility == Flagged)
                        || (b.type == Safe && b.visibility != Flagged));

        if (safeRevealed || minesFlagged) {
            end(Result.Victory);
            return;
        }

        setChanged();
        notifyObservers(Message.Update);
    }

    public void flagBlock(int x, int y) {
        if (result.isPresent())
            return;

        if (x < 0 || x > 9 || y < 0 || y > 9)
            return;

        Block block = blocks[y * 10 + x];

        if (block.visibility == Revealed)
            return;

        block.visibility = switch (block.visibility) {
            case Hidden -> Flagged;
            case Flagged -> Hidden;
            default -> block.visibility;
        };

        setChanged();
        notifyObservers(Message.Update);
    }

    public int flags() {
        return (int) Stream
                .of(blocks)
                .filter(block -> block.visibility == Flagged)
                .count();
    }

    public int time() {
        return time;
    }

    public Block[] blocks() {
        return blocks;
    }
}
