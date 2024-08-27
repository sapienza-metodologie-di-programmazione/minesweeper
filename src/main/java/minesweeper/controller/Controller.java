package minesweeper.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Console;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import minesweeper.model.Game;

import minesweeper.view.Canvas;

@SuppressWarnings("deprecation")
public class Controller {
    private Optional<ScheduledFuture<?>> timer;
    private ScheduledExecutorService scheduler;
    private Optional<Game> game;

    public Controller(minesweeper.model.Minesweeper model, minesweeper.view.Minesweeper view) {
        scheduler = Executors.newScheduledThreadPool(1);
        model.addObserver(view.menu());

        view.menu().play().addActionListener(e -> {
            Game game = new Game();
            game.addObserver(model);
            game.addObserver(view.game());
            game.addObserver(view.game().canvas());
            game.start();

            this.game = Optional.of(game);
            timer = Optional.of(scheduler.scheduleAtFixedRate(() -> game.update(), 1, 1, TimeUnit.SECONDS));
        });

        view.game().end().addActionListener(e -> {
            timer.ifPresent(t -> t.cancel(true));
            game.ifPresent(Game::end);
        });

        view.game().canvas().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                game.ifPresent(game -> {
                    Canvas canvas = view.game().canvas();

                    int x = (e.getX() - canvas.getWidth() / 2 + 5 * Canvas.SCALE) / 30;
                    int y = (e.getY() - canvas.getHeight() / 2 + 5 * Canvas.SCALE) / 30;

                    switch (e.getButton()) {
                        case MouseEvent.BUTTON1 -> game.tiles[y * 10 + x].reveal();
                        case MouseEvent.BUTTON3 -> game.tiles[y * 10 + x].flag();
                        default -> {
                        }
                    }
                });
            }
        });
    }

}

// minesweeper.model.Game game = model.newGame();
// view.game().canvas().game = Optional.of(game);
// view.game().canvas().game = Optional.empty();
