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

    public Menu() {
        setLayout(new GridBagLayout());

        games = ComponentFactory.whiteLabel("games played: " + Controller.getInstance().games());
        victories = ComponentFactory.whiteLabel("games won: " + Controller.getInstance().victories());

        Controller.getInstance().addObserver(this);

        add(new JPanel(new GridBagLayout()) {
            {
                var constraints = new GridBagConstraints();

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
                        JButton play = new JButton("Play");
                        play.addActionListener(e -> {
                            Controller.getInstance().startGame();
                            Navigator.getInstance().navigate(Screen.Game);
                        });

                        add(play);
                        add(games);
                        add(victories);
                    }
                }, constraints);
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        switch (o) {
            case Controller controller -> {
                if (arg instanceof Controller.Message) {
                    games.setText("games played: " + controller.games());
                    victories.setText("games won: " + controller.victories());
                }
            }
            default -> {
            }
        }
    }
}
