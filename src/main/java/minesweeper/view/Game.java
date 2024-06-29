package minesweeper.view;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import minesweeper.model.Block;

@SuppressWarnings("deprecation")
public class Game extends JPanel implements Observer {
    JLabel time, flags, mines;
    JPanel deck;

    Timer timer;
    int gameTime;

    minesweeper.model.Game game;

    Game(minesweeper.controller.Event event, JPanel deck) {
        setLayout(new BorderLayout());
        this.deck = deck;
        timer = new Timer(1000, e -> {
            gameTime++;
            time.setText("time: " + gameTime + "s");
        });

        add(new JPanel(new GridBagLayout()) {
            {
                setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                var constraints = new GridBagConstraints();

                constraints.weightx = 1;
                constraints.weighty = 1;
                constraints.anchor = GridBagConstraints.LINE_START;

                add(new JPanel(new GridLayout(1, 3, 10, 10)) {
                    {
                        add(time = ComponentFactory.whiteLabel("time: 0s"));
                        add(mines = ComponentFactory.whiteLabel("mines: 0s"));
                        add(flags = ComponentFactory.whiteLabel("flags: 0s"));
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
                            event.onGameTerminated();
                            Screen.show(deck, Screen.Menu);
                        });
                    }
                }, constraints);
            }
        }, BorderLayout.NORTH);

        class Canvas extends JPanel {
            Canvas() {
                super(null);
                var canvas = this;
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int scale = 30;
                        int x = (e.getX() - canvas.getWidth() / 2 + 5 * 30) / 30;
                        int y = (e.getY() - canvas.getHeight() / 2 + 5 * 30) / 30;

                        switch (e.getButton()) {
                            case MouseEvent.BUTTON1 -> event.onBlockRevealed(x, y);
                            case MouseEvent.BUTTON3 -> event.onBlockFlagged(x, y);
                            default -> {
                            }
                        }
                    }
                });
            }

            @Override
            public void paint(Graphics g) {
                super.paint(g);

                int scale = 30;
                g.translate(this.getWidth() / 2 - 5 * scale, this.getHeight() / 2 - 5 * scale);
                for (var block : game.blocks()) {
                    g.setColor(
                            switch (block.state()) {
                                case Flagged -> Color.RED;
                                case Hidden -> Color.LIGHT_GRAY;
                                case Revealed -> switch (block.type()) {
                                    case Safe -> Color.CYAN;
                                    case Mine -> Color.MAGENTA;
                                };
                            });

                    g.fillRect(block.x * scale, block.y * scale, scale, scale);

                    if (block.state() == Block.State.Revealed && block.type() == Block.Type.Safe) {
                        int neighbourMines = game.neighbourMines(block.x, block.y);

                        if (neighbourMines > 0) {
                            g.setColor(Color.BLACK);
                            g.drawString(String.valueOf(neighbourMines),
                                    block.x * scale + 10, block.y * scale + 20);
                        }
                    }

                    g.setColor(Color.BLACK);
                    g.drawRect(block.x * scale, block.y * scale, scale, scale);
                }
            }
        }

        add(new Canvas(), BorderLayout.CENTER);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof minesweeper.model.Game))
            return;

        if (arg instanceof minesweeper.model.Game.Result) {
            switch ((minesweeper.model.Game.Result) arg) {
                case Loss -> Screen.show(deck, Screen.Loss);
                case Victory -> Screen.show(deck, Screen.Victory);
                case Terminated -> Screen.show(deck, Screen.Menu);
                case New -> {
                    gameTime = 0;
                    timer.start();
                    game = (minesweeper.model.Game) o;
                    time.setText("time: " + gameTime + "s");
                    flags.setText("flags: " + game.flags());
                    mines.setText("mines: " + game.mines());
                    repaint();
                }
            }

            return;
        }

        flags.setText("flags: " + game.flags());
        mines.setText("mines: " + game.mines());
        repaint();

    }
}
