package minesweeper.view;

import java.util.Observable;

@SuppressWarnings("deprecation")
class Navigator extends Observable {

    static Navigator instance = new Navigator();

    private Navigator() {
    }

    static Navigator getInstance() {
        return instance;
    }

    void navigate(Screen screen) {
        setChanged();
        notifyObservers(screen);
    }

}

enum Screen {
    Menu,
    Game,
    Loss,
    Victory;
}
