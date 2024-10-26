package minesweeper.view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import javax.swing.JPanel;
import minesweeper.model.Tile;
import minesweeper.model.Game;

/**
 * The Canvas class is used to draw a Minesweeper game.
 *
 * @author Cicio Ionut
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class Canvas extends JPanel implements Observer {
    public static final int SCALE = 30;
    private Optional<Game> game = Optional.empty();

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        game.ifPresent(game -> {
            g.translate(this.getWidth() / 2 - 5 * SCALE, this.getHeight() / 2 - 5 * SCALE);
            g.setFont(View.FONT);

            for (Tile tile : game.tiles) {
                g.setColor(switch (tile.visibility()) {
                    case Flagged -> Color.RED;
                    case Hidden -> Color.LIGHT_GRAY;
                    case Revealed -> tile.isMine ? Color.MAGENTA : Color.CYAN;
                });

                g.fillRect(tile.x * SCALE, tile.y * SCALE, SCALE, SCALE);
                g.setColor(Color.BLACK);

                if (tile.visibility() == Tile.Visibility.Revealed)
                    tile.adjacentMines().ifPresent(
                            mines -> g.drawString(mines.toString(), tile.x * SCALE + 10, tile.y * SCALE + 20));

                g.drawRect(tile.x * SCALE, tile.y * SCALE, SCALE, SCALE);
            }
        });
    }

    /**
     * Updates when notified by a game.
     *
     * @param o   the game
     * @param arg the result of the game
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Game game)
            this.game = arg instanceof Game.Result ? Optional.empty() : Optional.of(game);
    }

}
