package minesweeper;

import minesweeper.controller.Controller;
import minesweeper.model.Minesweeper;
import minesweeper.view.View;

public class App {
    public static void main(String[] args) {
        new Controller(new Minesweeper(), new View());
    }
}
