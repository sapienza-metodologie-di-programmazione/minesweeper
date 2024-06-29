package minesweeper.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Menu extends JPanel {
    Menu(ActionListener onGameStarted) {
        setLayout(new GridBagLayout());

        add(new JPanel(new GridBagLayout()) {
            {
                var constraints = new GridBagConstraints();

                constraints.weightx = 1;
                constraints.weighty = 1;

                constraints.gridx = 0;
                constraints.gridy = 0;
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
                                addActionListener(onGameStarted);
                            }
                        });

                        add(new JLabel("games played: 5") {
                            {
                                setHorizontalAlignment(SwingConstants.CENTER);
                                setOpaque(true);
                                setBorder(Minesweeper.BORDER);
                            }
                        });

                        add(new JLabel("games won: 2") {
                            {
                                setHorizontalAlignment(SwingConstants.CENTER);
                                setOpaque(true);
                                setBorder(Minesweeper.BORDER);
                            }
                        });
                    }
                }, constraints);
            }
        });

    }
}
