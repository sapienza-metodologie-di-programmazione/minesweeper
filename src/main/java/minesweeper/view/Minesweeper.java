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

@SuppressWarnings("deprecation")
public class Minesweeper extends JFrame implements Observer {
    static Font FONT = new Font("Cascadia Code", Font.PLAIN, 14);
    static String LOGO = "https://static.wikia.nocookie.net/logopedia/images/9/98/Minesweeper_1992.png/revision/latest?cb=20220716174154";

    static {
        UIManager.put("Label.font", FONT);
        UIManager.put("Label.foreground", Color.DARK_GRAY);
        UIManager.put("Label.background", Color.WHITE);

        UIManager.put("Button.font", FONT);
        UIManager.put("Button.border", ComponentFactory.simpleBorder(new Color(224, 49, 49)));

        UIManager.put("Button.foreground", new Color(224, 49, 49));
        UIManager.put("Button.background", new Color(255, 201, 201));

        UIManager.put("Button.highlight", Color.WHITE);
        UIManager.put("Button.select", Color.WHITE);
        UIManager.put("Button.focus", Color.WHITE);

        UIManager.put("Panel.background", new Color(233, 236, 239));
    }

    JPanel deck;

    public Minesweeper() {
        super("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Navigator.getInstance().addObserver(this);

        try {
            setIconImage(ImageIO.read(new URL(LOGO)));
        } catch (Exception e) {
        }

        add(deck = new JPanel(new CardLayout()) {
            {
                add(new Menu(), Screen.Menu.name());

                add(new Game(), Screen.Game.name());

                add(new JPanel(new GridBagLayout()) {
                    {
                        setBackground(new Color(255, 201, 201));

                        add(new JButton("Game Over") {
                            {
                                addActionListener(e -> Navigator.getInstance().navigate(Screen.Menu));
                            }
                        });
                    }
                }, Screen.Loss.name());

                add(new JPanel(new GridBagLayout()) {
                    {
                        setBackground(new Color(178, 242, 187));

                        add(new JButton("Victory") {
                            {
                                setForeground(new Color(47, 158, 68));
                                setBackground(new Color(178, 242, 187));
                                setBorder(ComponentFactory.simpleBorder(new Color(47, 158, 68)));

                                addActionListener(e -> Navigator.getInstance().navigate(Screen.Menu));
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
        if (o instanceof Navigator && arg instanceof Screen)
            ((CardLayout) deck.getLayout()).show(deck, ((Screen) arg).name());
    }
}
