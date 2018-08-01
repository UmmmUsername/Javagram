package gui.overlays;

import components.ImageButton;
import gui.resources.Images;

import javax.swing.*;
import java.awt.event.ActionListener;

public class PlusOverlay extends JPanel {
    private JButton plusButton;
    private JPanel rootPanel;

    private void createUIComponents() {
        rootPanel = this;

        plusButton = new ImageButton(Images.getPlusIcon());
    }

    public void addActionListener(ActionListener actionListener) {
        plusButton.addActionListener(actionListener);
    }

    public void removeActionListener(ActionListener actionListener) {
        plusButton.removeActionListener(actionListener);
    }
}
