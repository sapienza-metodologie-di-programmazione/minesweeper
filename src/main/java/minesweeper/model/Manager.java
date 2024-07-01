package minesweeper.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.Vector;

import static minesweeper.model.Game.Result.*;

@SuppressWarnings("deprecation")
public class Manager extends Observable implements Observer {
    Vector<Game> games = new Vector<>();
    static final String FILE = "games.db";

    public Manager() {
        // TODO: check if list is valid

        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(FILE));

            @SuppressWarnings("unchecked")
            Vector<Game> games = (Vector<Game>) stream.readObject();
            this.games = games;

            stream.close();
        } catch (IOException | ClassNotFoundException e) {
        }

    }

    void serialize() {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(FILE));
            stream.writeObject(games);
            stream.close();

            setChanged();
            notifyObservers();
        } catch (IOException e) {
        }
    }

    public void startGame(Observer... observers) {
        game().ifPresent(Game::terminate);
        games.add(new Game(observers));
        game().ifPresent(game -> game.addObserver(this));
    }

    public Optional<Game> game() {
        try {
            if (games.lastElement().result.isEmpty())
                return Optional.of(games.lastElement());

            return Optional.empty();
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    public int games() {
        return games.size();
    }

    public int vitories() {
        return (int) games
                .stream()
                .filter(game -> game.result == Optional.of(Victory))
                .count();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Game && arg instanceof Game.Result)
            serialize();
    }
}
