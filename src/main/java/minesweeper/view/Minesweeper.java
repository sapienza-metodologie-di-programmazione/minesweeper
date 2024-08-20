package minesweeper.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import minesweeper.controller.Controller;

@SuppressWarnings("deprecation")
public class Minesweeper extends JFrame implements Observer {
    static Font FONT = new Font("Cascadia Code", Font.PLAIN, 14);
    static String LOGO = "https://static.wikia.nocookie.net/logopedia/images/9/98/Minesweeper_1992.png/revision/latest?cb=20220716174154";

    static {
        UIManager.put("Label.font", FONT);
        UIManager.put("Label.foreground", Color.DARK_GRAY);
        UIManager.put("Label.background", Color.WHITE);

        UIManager.put("Button.font", FONT);
        UIManager.put("Button.border", Factory.border(new Color(224, 49, 49)));

        UIManager.put("Button.foreground", new Color(224, 49, 49));
        UIManager.put("Button.background", new Color(255, 201, 201));

        UIManager.put("Button.highlight", Color.WHITE);
        UIManager.put("Button.select", Color.WHITE);
        UIManager.put("Button.focus", Color.WHITE);

        UIManager.put("Panel.background", new Color(233, 236, 239));
    }

    JPanel deck;

    public Minesweeper(Controller controller) {
        super("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            setIconImage(ImageIO.read(new URL(LOGO)));
        } catch (Exception e) {
        }

        Navigator navigator = new Navigator();
        navigator.addObserver(this);

        add(deck = new JPanel(new CardLayout()) {
            {
                add(new Menu(controller, navigator), Screen.Menu.name());

                add(new Game(controller, navigator), Screen.Game.name());

                add(new JPanel(new GridBagLayout()) {
                    {
                        setBackground(new Color(255, 201, 201));
                        JButton gameOver = new JButton("Game Over");
                        gameOver.addActionListener(e -> navigator.navigate(Screen.Menu));
                        add(gameOver);
                    }
                }, Screen.Loss.name());

                add(new JPanel(new GridBagLayout()) {
                    {
                        setBackground(new Color(178, 242, 187));

                        add(new JButton("Victory") {
                            {
                                setForeground(new Color(47, 158, 68));
                                setBackground(new Color(178, 242, 187));
                                setBorder(Factory.border(new Color(47, 158, 68)));

                                addActionListener(e -> navigator.navigate(Screen.Menu));
                            }
                        });
                    }
                }, Screen.Victory.name());
            }
        });

        setSize(740, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Navigator && arg instanceof Screen screen)
            ((CardLayout) deck.getLayout()).show(deck, screen.name());
    }
}

// add(new JButton("Game Over") {
// {
// addActionListener(e -> navigator.navigate(Screen.Menu));
// }
// });

// Navigator navigator = Navigator.getInstance();
// Navigator.getInstance().addObserver(this);

// JButton gameOver = new JButton("Game Over");
// gameOver.addActionListener(e -> navigator.navigate(Screen.Menu));
// add(gameOver);

// switch (arg) {
// case Screen screen -> ((CardLayout) deck.getLayout()).show(deck,
// screen.name());
// default -> {
// }
// }
