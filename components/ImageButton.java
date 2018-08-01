package components;

import javax.swing.*;
import java.awt.*;

public class ImageButton extends JButton {
    private Image image;

    public ImageButton(Image image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        Rectangle rectangle = GuiHelper.getAreaToFill(
                getSize(), new Dimension(image.getWidth(null), image.getHeight(null)));
        g.drawImage(image, rectangle.x, rectangle.y, rectangle.width, rectangle.height, null);
    }

    @Override
    protected void paintBorder(Graphics g) {
    }
}
