package minesweeper.view;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

final class ComponentFactory {
    private ComponentFactory() {
    }

    static JLabel whiteLabel(String text) {
        return new JLabel(text) {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
                setOpaque(true);
                setBackground(Color.WHITE);
                setBorder(Minesweeper.BORDER);
            }
        };
    }
}
