package minesweeper.controller;

public interface GameListener {
    void onBlockRevealed(int x, int y);

    void onBlockFlagged(int x, int y);

    void onGameTerminated();
}
