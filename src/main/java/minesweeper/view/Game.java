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

import minesweeper.model.Game.Message;
import minesweeper.model.Game.Result;
import minesweeper.controller.GameListener;
import minesweeper.model.Block;

@SuppressWarnings("deprecation")
public class Game extends JPanel implements Observer {
    JLabel time, flags, mines;

    GameListener gameListener;
    // Block[] blocks;
    minesweeper.model.Game game;

    public Game(GameListener gameListener) {
        setLayout(new BorderLayout());

        this.gameListener = gameListener;

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
                            gameListener.onGameTerminated();
                            Navigator.getInstance().navigate(Screen.Menu);
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
                            case MouseEvent.BUTTON1 -> gameListener.onBlockRevealed(x, y);
                            case MouseEvent.BUTTON3 -> gameListener.onBlockFlagged(x, y);
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
                            switch (block.visibility()) {
                                case Flagged -> Color.RED;
                                case Hidden -> Color.LIGHT_GRAY;
                                case Revealed -> switch (block.type) {
                                    case Safe -> Color.CYAN;
                                    case Mine -> Color.MAGENTA;
                                };
                            });

                    g.fillRect(block.x * scale, block.y * scale, scale, scale);

                    if (block.visibility() == Block.Visibility.Revealed && block.type == Block.Type.Safe) {
                        int neighbourMines = game.neighbourMines(block.x, block.y);

                        if (neighbourMines > 0) {
                            g.setFont(Minesweeper.FONT);
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

        minesweeper.model.Game game = (minesweeper.model.Game) o;

        if (arg instanceof Result)
            Navigator.getInstance().navigate(
                    switch ((Result) arg) {
                        case Loss -> Screen.Loss;
                        case Victory -> Screen.Victory;
                        case Terminated -> Screen.Menu;
                    });

        if (arg instanceof Message)
            switch ((Message) arg) {
                case Start -> {
                    flags.setText("flags: " + game.flags());
                    mines.setText("mines: " + game.mines);
                    time.setText("time: " + 0 + "s");
                    repaint();
                }
                case Update -> {
                    flags.setText("flags: " + game.flags());
                    mines.setText("mines: " + game.mines);
                    repaint();
                }
                case Timer -> {
                    time.setText("time: " + game.time() + "s");
                }
            }
    }
}
