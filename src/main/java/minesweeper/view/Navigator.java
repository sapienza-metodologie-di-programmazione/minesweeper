package minesweeper.view;

import java.util.Observable;

/**
 * The Navigator class is used to change view screens.
 *
 * @author Cicio Ionut
 * @version 1.0
 */
@SuppressWarnings("deprecation")
class Navigator extends Observable {
    void navigate(Screen screen) {
        setChanged();
        notifyObservers(screen);
    }
}

/**
 * The Screen enum represents the available screens in the view.
 *
 * @author Cicio Ionut
 * @version 1.0
 */
enum Screen {
    Menu, Game, Loss, Victory
}
