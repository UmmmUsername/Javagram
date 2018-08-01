package components;

import java.awt.*;

public class ImageAndTextButton extends ImageButton {
    private boolean right;
    private int rightInset;

    public ImageAndTextButton(Image image, boolean right, int rightInset) {
        super(image);

        this.right = right;
        this.rightInset = rightInset;
    }

    public ImageAndTextButton(Image image) {
        this(image, false, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        String text = getText();
        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);

        int x;

        if (right) {
            x = getWidth() - textWidth - rightInset;
        } else {
            x = (getWidth() - textWidth) / 2;
        }

        int y = getBaseline(getWidth(), getHeight());
        g.setColor(getForeground());
        g.drawString(text, x, y);
    }
}
