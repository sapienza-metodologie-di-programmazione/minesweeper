package minesweeper.view;

import static minesweeper.model.Tile.Visibility.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import minesweeper.controller.Controller;
import minesweeper.model.Game.Result;
import minesweeper.model.Tile;

@SuppressWarnings("deprecation")
public class Game extends JPanel implements Observer {

    private JLabel time, flags, mines;
    private Navigator navigator;

    Optional<minesweeper.model.Game> game = Optional.empty();

    public Game(Controller controller, Navigator navigator) {
        setLayout(new BorderLayout());

        controller.addObserver(this);
        this.navigator = navigator;

        add(new JPanel(new GridBagLayout()) {
            {
                setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                var constraints = new GridBagConstraints();

                constraints.weightx = 1;
                constraints.weighty = 1;
                constraints.anchor = GridBagConstraints.LINE_START;

                add(new JPanel(new GridLayout(1, 3, 10, 10)) {
                    {
                        add(time = Factory.label("time: 0s"));
                        add(mines = Factory.label("mines: 0"));
                        add(flags = Factory.label("flags: 0"));
                    }
                }, constraints);

                constraints.gridx = 1;
                constraints.fill = GridBagConstraints.BOTH;

                add(new JPanel(), constraints);

                constraints.anchor = GridBagConstraints.LINE_END;
                constraints.fill = GridBagConstraints.NONE;
                constraints.gridx = 2;

                add(new JButton("end") {
                    {
                        addActionListener(e -> {
                            controller.endGame();
                            navigator.navigate(Screen.Menu);
                        });
                    }
                }, constraints);
            }
        }, BorderLayout.NORTH);

        class Canvas extends JPanel {
            static int SIZE = 30;

            Canvas() {
                Canvas canvas = this;

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        game.ifPresent(game -> {
                            int x = (e.getX() - canvas.getWidth() / 2 + 5 * SIZE) / 30;
                            int y = (e.getY() - canvas.getHeight() / 2 + 5 * SIZE) / 30;

                            switch (e.getButton()) {
                                case MouseEvent.BUTTON1 -> controller.reveal(x, y);
                                case MouseEvent.BUTTON3 -> controller.flag(x, y);
                                default -> {
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void paint(Graphics g) {
                super.paint(g);

                game.ifPresent(game -> {
                    g.translate(this.getWidth() / 2 - 5 * SIZE, this.getHeight() / 2 - 5 * SIZE);

                    for (Tile tile : game.tiles) {
                        g.setColor(switch (tile.visibility()) {
                            case Flagged -> Color.RED;
                            case Hidden -> Color.LIGHT_GRAY;
                            case Revealed -> switch (tile.kind) {
                                case Empty -> Color.CYAN;
                                case Mine -> Color.MAGENTA;
                            };
                        });

                        g.fillRect(tile.x * SIZE, tile.y * SIZE, SIZE, SIZE);

                        if (tile.visibility() == Revealed)
                            tile.adjacentMines().ifPresent(mines -> {
                                g.setFont(Minesweeper.FONT);
                                g.setColor(Color.BLACK);
                                g.drawString(mines.toString(), tile.x * SIZE + 10, tile.y * SIZE + 20);
                            });

                        g.setColor(Color.BLACK);
                        g.drawRect(tile.x * SIZE, tile.y * SIZE, SIZE, SIZE);
                    }
                });
            }
        }

        add(new Canvas(), BorderLayout.CENTER);
    }

    @Override
    public void update(Observable o, Object arg) {
        switch (o) {
            case Controller controller -> {
                if (arg instanceof minesweeper.model.Game game) {
                    game.addObserver(this);
                    Stream.of(game.tiles).forEach(t -> t.addObserver(this));

                    mines.setText("mines: " + game.mines);
                    flags.setText("flags: " + game.flags());
                    time.setText("time: " + game.time().toSeconds() + "s");

                    this.game = Optional.of(game);
                    repaint();
                }
            }
            case minesweeper.model.Game game -> {
                if (arg instanceof Result result)
                    navigator.navigate(switch (result) {
                        case Loss -> Screen.Loss;
                        case Victory -> Screen.Victory;
                        case Terminated -> Screen.Menu;
                    });

                if (arg instanceof Duration duration) {
                    time.setText("time: " + duration.toSeconds() + "s");
                    repaint();
                }
            }
            case Tile tile -> {
                if (arg instanceof Tile.Visibility visibility) {
                    if (visibility != Revealed)
                        game.ifPresent(game -> flags.setText("flags: " + game.flags()));

                    repaint();
                }
            }
            default -> {
            }
        }
    }

}

// switch (arg) {
// case Result result ->
// case minesweeper.model.Game.Message message -> {
// switch (message) {
// case Timer -> {
// time.setText("time: " + game.time().toSeconds() + "s");
// repaint();
// }
// }
// }
// default -> {
// time.setText("time: " + game.time().toSeconds() + "s");
// repaint();
// }

// switch (arg) {
// case Tile.Visibility visibility -> {
// if (visibility != Revealed)
// game.ifPresent(game -> flags.setText("flags: " + game.flags()));
//
// repaint();
// }
// default -> {
// }
// }

// Controller.getInstance().addObserver(this);

// case MouseEvent.BUTTON1 -> game.tiles[y * 10 + x].reveal();
// case MouseEvent.BUTTON3 -> game.tiles[y * 10 + x].flag();

// Navigator.getInstance().navigate(switch (result) {

// Controller.getInstance().endGame();
// Navigator.getInstance().navigate(Screen.Menu);
