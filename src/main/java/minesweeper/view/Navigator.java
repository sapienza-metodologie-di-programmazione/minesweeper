package minesweeper.view;

import java.util.Observable;

enum Screen {
    Menu,
    Game,
    Loss,
    Victory;
}

@SuppressWarnings("deprecation")
public class Navigator extends Observable {
    static Navigator instance = new Navigator();

    private Navigator() {
    }

    public static Navigator getInstance() {
        return instance;
    }

    void navigate(Screen screen) {
        setChanged();
        notifyObservers(screen);
    }
}
