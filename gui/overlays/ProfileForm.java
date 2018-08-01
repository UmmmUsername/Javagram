package gui.overlays;

import components.ImageButton;
import components.ImagePanel;
import components.OverlayBackground;
import gui.resources.Images;
import org.javagram.dao.ApiException;
import org.javagram.dao.Me;
import org.javagram.dao.proxy.TelegramProxy;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ProfileForm extends OverlayBackground {
    private JButton exitButton;
    private JButton backButton;
    private JPanel rootPanel;
    private JLabel nameLabel;
    private JLabel phoneLabel;
    private JPanel photoPanel;

    private final static String phoneRegexFrom = "^\\+?(\\d*)(\\d{3})(\\d{3})(\\d{2})(\\d{2})$", phoneRegexTo = "+$1 ($2) $3-$4-$5";

    private void createUIComponents() {
        rootPanel = this;

        photoPanel = new ImagePanel(null, true, true, 0);
        backButton = new ImageButton(Images.getBackIcon());
    }

    public void setInfo(ContactInfo info) {
        if (info != null) {
            ((ImagePanel) photoPanel).setImage(info.getPhoto());
            nameLabel.setText(info.getFirstName() + " " + info.getLastName());
            phoneLabel.setText(info.getPhone().replaceAll(phoneRegexFrom, phoneRegexTo));
        } else {
            ((ImagePanel) photoPanel).setImage(null);
            nameLabel.setText("");
            phoneLabel.setText("");
        }
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void removeBackButtonListener(ActionListener listener) {
        backButton.removeActionListener(listener);
    }

    public void addExitButtonListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }

    public void removeExitButtonListener(ActionListener listener) {
        exitButton.removeActionListener(listener);
    }
}