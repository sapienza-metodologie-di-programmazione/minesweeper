package minesweeper.controller;

import java.util.Observer;

@SuppressWarnings("deprecation")
public interface ControlListener {
    void onGameStarted(Observer... observers);
}
