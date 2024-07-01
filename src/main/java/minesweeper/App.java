package minesweeper;

import java.util.Observer;

import minesweeper.controller.ControlListener;
import minesweeper.controller.GameListener;

@SuppressWarnings("deprecation")
public class App {
    public static void main(String[] args) {
        var manager = new minesweeper.model.Manager();

        ControlListener controlListener = new ControlListener() {
            @Override
            public void onGameStarted(Observer... observers) {
                manager.startGame(observers);
            }
        };

        GameListener gameListener = new GameListener() {
            @Override
            public void onBlockRevealed(int x, int y) {
                manager.game().ifPresent(game -> game.revealBlock(x, y));
            }

            @Override
            public void onBlockFlagged(int x, int y) {
                manager.game().ifPresent(game -> game.flagBlock(x, y));
            }

            @Override
            public void onGameTerminated() {
                manager.game().ifPresent(minesweeper.model.Game::terminate);
            }
        };

        var game = new minesweeper.view.Game(gameListener);

        minesweeper.view.Menu menu = new minesweeper.view.Menu(e -> controlListener.onGameStarted(game));

        new minesweeper.view.Minesweeper(menu, game);
    }
}

// javax.swing.UIManager.getDefaults().keys().asIterator().forEachRemaining(System.out::println);
