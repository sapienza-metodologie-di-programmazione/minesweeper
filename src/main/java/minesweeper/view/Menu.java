package minesweeper.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import minesweeper.controller.Controller;

@SuppressWarnings("deprecation")
public class Menu extends JPanel implements Observer {

    JLabel games, victories;

    public Menu(Controller controller, Navigator navigator) {
        super(new GridBagLayout());
        controller.addObserver(this);

        add(new JPanel(new GridBagLayout()) {
            {
                GridBagConstraints constraints = new GridBagConstraints();

                constraints.weightx = 1;
                constraints.weighty = 1;
                constraints.insets.bottom = 20;

                add(new JLabel("Minesweeper") {
                    {
                        setFont(getFont().deriveFont(35f));
                        setHorizontalAlignment(SwingConstants.CENTER);
                        setForeground(Color.BLACK);
                    }
                }, constraints);

                constraints.gridy = 1;

                add(new JPanel(new GridLayout(3, 1, 10, 10)) {
                    {
                        add(new JButton("Play") {
                            {
                                addActionListener(e -> {
                                    controller.startGame();
                                    navigator.navigate(Screen.Game);
                                });
                            }
                        });

                        add(games = Factory.label("games played: " + controller.games()));
                        add(victories = Factory.label("games won: " + controller.victories()));
                    }
                }, constraints);
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Controller controller && arg instanceof Controller.Message) {
            games.setText("games played: " + controller.games());
            victories.setText("games won: " + controller.victories());
        }
    }

}

// games = Factory.label("games played: " + controller.games());
// victories = Factory.label("games won: " + controller.victories());

// TODO: controller and navigator?
// Controller.getInstance().addObserver(this);

// Controller.getInstance().startGame();
// Navigator.getInstance().navigate(Screen.Game);

// if () {
// }
// switch (o) {
// case Controller controller -> {
// if (arg instanceof Controller.Message) {
// games.setText("games played: " + controller.games());
// victories.setText("games won: " + controller.victories());
// }
// }
// default -> {
// }
// }

// JButton play = new JButton("Play");
// play.addActionListener(e -> {
// controller.startGame();
// navigator.navigate(Screen.Game);
// });
//
// add(play);
