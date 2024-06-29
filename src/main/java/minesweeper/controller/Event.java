package minesweeper.controller;

import java.util.Observer;

@SuppressWarnings("deprecation")
public interface Event {
    void onGameStarted(Observer view);

    void onGameTerminated();

    void onBlockRevealed(int x, int y);

    void onBlockFlagged(int x, int y);
}
