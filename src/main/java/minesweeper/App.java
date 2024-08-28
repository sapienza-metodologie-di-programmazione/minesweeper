package minesweeper;

import minesweeper.controller.Controller;

public class App {
    public static void main(String[] args) {
        new Controller(new minesweeper.model.Minesweeper(), new minesweeper.view.Minesweeper());
    }
}
