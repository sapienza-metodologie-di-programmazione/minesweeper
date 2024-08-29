package minesweeper.view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * The Factory class is used to create Swing components.
 *
 * @author Cicio Ionut
 * @version 1.0
 */
final class Factory {
    private Factory() {
    }

    /**
     * Creates a simple compound border using the specified color.
     *
     * @param color the color of the border
     * @return the simple compound border
     */
    static Border border(Color color) {
        return BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color),
                BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

    /**
     * Creates a simple JLabel with the specified text.
     *
     * @param text the text of the label
     * @return the simple JLabel
     */
    static JLabel label(String text) {
        return new JLabel(text) {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
                setOpaque(true);
                setBackground(Color.WHITE);
                setBorder(Factory.border(Color.BLACK));
            }
        };
    }
}
