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

@SuppressWarnings("deprecation")
public class Menu extends JPanel implements Observer {

    JLabel games, victories;
    JButton play;

    public Menu(Navigator navigator) {
        super(new GridBagLayout());

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
                        add(play = new JButton("Play") {
                            {
                                addActionListener(e -> navigator.navigate(Screen.Game));
                            }
                        });

                        add(games = Factory.label("games played: 0"));
                        add(victories = Factory.label("games won: 0"));
                    }
                }, constraints);
            }
        });
    }

    public JButton play() {
        return play;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof minesweeper.model.Minesweeper minesweeper) {
            games.setText("games played: " + minesweeper.games());
            victories.setText("games won: " + minesweeper.victories());
        }
    }

}
