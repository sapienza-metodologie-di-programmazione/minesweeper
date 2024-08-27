package minesweeper.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("deprecation")
public class Minesweeper extends Observable implements Observer {

    private static final String DATABASE = "games.db";
    private int games = 0, victories = 0;

    public Minesweeper() {
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(DATABASE));
            this.games = (Integer) stream.readObject();
            this.victories = (Integer) stream.readObject();
            stream.close();
        } catch (IOException | ClassNotFoundException e) {
        }
    }

    public int games() {
        return games;
    }

    public int victories() {
        return victories;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof Game && arg instanceof Game.Result result))
            return;

        games++;
        if (result == Game.Result.Victory)
            victories++;

        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(DATABASE));
            stream.writeObject(games);
            stream.writeObject(victories);
            stream.close();
        } catch (IOException e) {
        }

        setChanged();
        notifyObservers();
    }

}

// public Game newGame() {
// Game game = new Game();
// game.addObserver(this);
// return game;
// }

// this.games = games;
// this.victories = victories;

// import java.util.Optional;

// private Optional<Game> game = Optional.empty();
// game.ifPresent(Game::end);
// this.game = Optional.of(game);
// game = Optional.empty();

// public void endGame() {
// game.ifPresent(Game::end);
// }
