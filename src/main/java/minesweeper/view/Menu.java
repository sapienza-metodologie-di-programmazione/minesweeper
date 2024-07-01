package minesweeper.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("deprecation")
public class Menu extends JPanel implements Observer {
    JLabel gamesPlayed, gamesWon;

    public Menu(ActionListener onGameStarted) {
        setLayout(new GridBagLayout());

        gamesPlayed = ComponentFactory.whiteLabel("games played: 0");
        gamesWon = ComponentFactory.whiteLabel("games won: 0");

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
                        add(new JButton("Play") {
                            {
                                addActionListener(e -> {
                                    onGameStarted.actionPerformed(e);
                                    Navigator.getInstance().navigate(Screen.Game);
                                });
                            }
                        });

                        add(gamesPlayed);
                        add(gamesWon);

                    }
                }, constraints);
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof minesweeper.model.Minesweeper))
            return;

        var model = (minesweeper.model.Minesweeper) o;
        gamesPlayed.setText("games played: " + model.games());
        gamesWon.setText("games won: " + model.vitories());
    }
}
