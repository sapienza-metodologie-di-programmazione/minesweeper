package minesweeper.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.stream.Stream;

// TODO: at least 10 mines, at most 20

@SuppressWarnings("deprecation")
public class Game extends Observable {
    public enum Result {
        Loss,
        Victory,
        New,
        Terminated
    }

    Block[] blocks;
    int mines;

    public Game() {
        Random random = new Random();

        blocks = new Block[100];

        for (int y = 0; y < 10; y++)
            for (int x = 0; x < 10; x++) {
                Block.Type type = random.nextInt(100) >= 15 ? Block.Type.Safe : Block.Type.Mine;
                blocks[y * 10 + x] = new Block(x, y, type);
            }

        mines = (int) Stream.of(blocks)
                .map(block -> block.type)
                .filter(type -> type == Block.Type.Mine)
                .count();

        setChanged();
    }

    List<Block> neighbours(int x, int y) {
        return List.of(
                new Point(x - 1, y - 1),
                new Point(x, y - 1),
                new Point(x + 1, y - 1),
                new Point(x - 1, y),
                new Point(x + 1, y),
                new Point(x - 1, y + 1),
                new Point(x, y + 1),
                new Point(x + 1, y + 1) //
        )
                .stream()
                .filter(p -> p.x >= 0 && p.x < 10 && p.y >= 0 && p.y < 10)
                .map(p -> blocks[p.y * 10 + p.x])
                .toList();
    }

    public int neighbourMines(int x, int y) {
        return (int) neighbours(x, y).stream().filter(block -> block.type == Block.Type.Mine).count();
    }

    public void terminate() {
        setChanged();
        notifyObservers(Game.Result.Terminated);
        deleteObservers();
    }

    public void revealBlock(int x, int y) throws Exception {
        if (x < 0 || x > 9 || y < 0 || y > 9)
            throw new Exception("block is out of bounds");

        var block = blocks[y * 10 + x];

        if (block.state != Block.State.Hidden)
            return;

        block.state = Block.State.Revealed;
        setChanged();

        if (block.type == Block.Type.Mine) {
            notifyObservers(Result.Loss);
            return;
        }

        if (neighbourMines(x, y) == 0)
            for (var neighbour : neighbours(x, y))
                revealBlock(neighbour.x, neighbour.y);

        boolean victory = Stream.of(blocks)
                .allMatch(b -> b.state == Block.State.Revealed || b.type == Block.Type.Mine)
                ||
                Stream.of(blocks)
                        .allMatch(b -> b.state == Block.State.Flagged && b.type == Block.Type.Mine);

        if (victory) {
            notifyObservers(Result.Victory);
            return;
        }

        notifyObservers();
    }

    public void flagBlock(int x, int y) throws Exception {
        if (x < 0 || x > 9 || y < 0 || y > 9)
            throw new Exception("block is out of bounds");

        var block = blocks[y * 10 + x];

        if (block.state == Block.State.Revealed)
            return;

        block.state = switch (block.state) {
            case Hidden -> Block.State.Flagged;
            case Flagged -> Block.State.Hidden;
            default -> block.state;
        };

        setChanged();
        notifyObservers();
    }

    public int mines() {
        return mines;
    }

    public int flags() {
        return (int) Stream.of(blocks).filter(block -> block.state == Block.State.Flagged).count();
    }

    public Block[] blocks() {
        return blocks;
    }
}
