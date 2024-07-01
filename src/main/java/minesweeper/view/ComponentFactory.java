package minesweeper.view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

final class ComponentFactory {
    private ComponentFactory() {
    }

    static Border simpleBorder(Color color) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color),
                BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

    static JLabel whiteLabel(String text) {
        return new JLabel(text) {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
                setOpaque(true);
                setBackground(Color.WHITE);
                setBorder(ComponentFactory.simpleBorder(Color.BLACK));
            }
        };
    }
}
