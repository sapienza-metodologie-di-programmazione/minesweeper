package minesweeper;

import java.util.Observer;
import java.util.Vector;

// javax.swing.UIManager.getDefaults().keys().asIterator().forEachRemaining(System.out::println);

@SuppressWarnings("deprecation")
public class App {
    public static void main(String[] args) {
        Vector<minesweeper.model.Game> games = new Vector<>();
        // TODO: read list from file

        minesweeper.controller.Event event = new minesweeper.controller.Event() {
            @Override
            public void onGameStarted(Observer observer) {
                var game = new minesweeper.model.Game();
                games.add(game);
                game.addObserver(observer);
                game.notifyObservers(minesweeper.model.Game.Result.New);
            }

            @Override
            public void onGameTerminated() {
                games.lastElement().terminate();
                // TODO: save game data as terminated!
            }

            @Override
            public void onBlockRevealed(int x, int y) {
                try {
                    games.lastElement().revealBlock(x, y);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBlockFlagged(int x, int y) {
                try {
                    games.lastElement().flagBlock(x, y);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new minesweeper.view.Minesweeper(event);
    }
}
