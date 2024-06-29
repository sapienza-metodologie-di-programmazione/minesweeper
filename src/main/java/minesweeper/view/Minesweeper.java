package minesweeper.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;

enum Screen {
    Menu,
    Game,
    Loss,
    Victory;

    static void show(JPanel panel, Screen screen) {
        ((CardLayout) panel.getLayout()).show(panel, screen.name());
    }
}

public class Minesweeper extends JFrame {
    static Border BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(10, 15, 10, 15));

    public Minesweeper(minesweeper.controller.Event event) {
        super("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var cascadiaCode = new Font("Cascadia Code", Font.PLAIN, 14);
        var darkRed = new Color(224, 49, 49);
        var lightRed = new Color(255, 201, 201);

        UIManager.put("Label.font", cascadiaCode);
        UIManager.put("Label.foreground", Color.DARK_GRAY);
        UIManager.put("Label.background", Color.WHITE);

        UIManager.put("Button.font", cascadiaCode);
        UIManager.put("Button.foreground", darkRed);
        UIManager.put("Button.background", lightRed);
        UIManager.put("Button.highlight", darkRed);
        UIManager.put("Button.select", new Color(255, 190, 190));
        UIManager.put("Button.focus", new Color(255, 190, 190));
        UIManager.put("Button.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 49, 49)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        UIManager.put("Panel.background", new Color(233, 236, 239));

        try {
            setIconImage(ImageIO.read(new URL(
                    "https://static.wikia.nocookie.net/logopedia/images/9/98/Minesweeper_1992.png/revision/latest?cb=20220716174154")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(new JPanel(new CardLayout()) {
            {
                var game = new Game(event, this);
                var manager = this;

                add(game, Screen.Game.name());

                add(new Menu(e -> {
                    event.onGameStarted(game);
                    Screen.show(this, Screen.Game);
                }), Screen.Menu.name());

                add(new JPanel(new GridBagLayout()) {
                    {
                        setBackground(lightRed);

                        add(new JButton("Game Over") {
                            {
                                addActionListener(e -> Screen.show(manager, Screen.Menu));
                            }
                        });
                    }
                }, Screen.Loss.name());

                add(new JPanel(new GridBagLayout()) {
                    {
                        setBackground(lightRed);

                        add(new JButton("Victory") {
                            {
                                addActionListener(e -> Screen.show(manager, Screen.Menu));
                            }
                        });
                    }
                }, Screen.Victory.name());

                Screen.show(this, Screen.Menu);
                // add(game, Screen.Game.name());
                // add(new Game(e -> Screen.show(this, Screen.Menu)), Screen.Game.name());
            }

        });

        // setSize(1280, 800);
        setSize(740, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
