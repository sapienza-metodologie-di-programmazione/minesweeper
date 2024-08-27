package minesweeper.view;

import java.util.Observable;

@SuppressWarnings("deprecation")
class Navigator extends Observable {
    void navigate(Screen screen) {
        setChanged();
        notifyObservers(screen);
    }
}

enum Screen {
    Menu, Game, Loss, Victory
}
