package minesweeper.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import minesweeper.model.Game;

@SuppressWarnings("deprecation")
public class Controller extends Observable implements Observer {

    public enum Message {
        Save
    }

    static final String FILE = "games.db";
    static Controller instance = new Controller();

    Optional<Game> game;
    int games, victories;
    Optional<ScheduledFuture<?>> timer;
    ScheduledExecutorService scheduler;

    private Controller() {
        scheduler = Executors.newScheduledThreadPool(1);

        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(FILE));

            Integer games = (Integer) stream.readObject();
            Integer victories = (Integer) stream.readObject();
            this.games = games;
            this.victories = victories;

            stream.close();
        } catch (IOException | ClassNotFoundException e) {
            games = 0;
            victories = 0;
        }
    }

    public static Controller getInstance() {
        return instance;
    }

    public void startGame() {
        Game game = new Game();
        this.game = Optional.of(game);
        game.addObserver(this);

        setChanged();
        notifyObservers(game);

        timer = Optional.of(scheduler.scheduleAtFixedRate(() -> game.updateTime(), 1, 1, TimeUnit.SECONDS));
    }

    public void endGame() {
        timer.ifPresent(timer -> timer.cancel(true));
        game.ifPresent(Game::end);
        game = Optional.empty();
    }

    public int games() {
        return games;
    }

    public int victories() {
        return victories;
    }

    @Override
    public void update(Observable o, Object arg) {
        switch (o) {
            case Game game -> {
                switch (arg) {
                    case Game.Result result -> {
                        games++;

                        if (result == Game.Result.Victory)
                            victories++;

                        try {
                            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(FILE));

                            stream.writeObject(games);
                            stream.writeObject(victories);

                            stream.close();
                        } catch (IOException e) {
                        }

                        setChanged();
                        notifyObservers(Message.Save);
                    }
                    default -> {
                    }
                }
            }
            default -> {
            }
        }

    }

}
