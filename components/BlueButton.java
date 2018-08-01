package components;

import gui.resources.Images;

import java.awt.*;

public class BlueButton extends ImageButton {
    public BlueButton() {
        super(Images.getBlueButton());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        String text = getText();
        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        int yInset = 5;

        int x = (getWidth() - textWidth) / 2;
        int y = getBaseline(getWidth(), getHeight());
        g.setColor(getForeground());
        g.drawString(text, x, y - yInset);
    }
}
