package minesweeper;

public class App {
    public static void main(String[] args) {
        new minesweeper.view.Minesweeper(new minesweeper.controller.Controller());
    }
}

// new minesweeper.controller.Controller(new minesweeper.model.Game(), new
// minesweeper.view.Minesweeper);
