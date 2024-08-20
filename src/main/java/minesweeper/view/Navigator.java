package minesweeper.view;

import java.util.Observable;

@SuppressWarnings("deprecation")
class Navigator extends Observable {
    Screen screen = Screen.Menu;

    void navigate(Screen screen) {
        if (this.screen == screen)
            return;

        setChanged();
        notifyObservers(this.screen = screen);
    }
}

enum Screen {
    Menu,
    Game,
    Loss,
    Victory;
}

// static Navigator instance;

// private Navigator() {
// }

// static Navigator getInstance() {
// if (instance == null)
// instance = new Navigator();
// return instance;
// }
