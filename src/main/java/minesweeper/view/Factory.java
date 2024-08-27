package minesweeper.view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

final class Factory {
    private Factory() {
    }

    static Border border(Color color) {
        return BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color),
                BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

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
