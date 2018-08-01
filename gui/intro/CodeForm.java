package gui.intro;

import components.BlueButton;
import components.ImagePanel;
import gui.Helper;
import gui.resources.Fonts;
import gui.resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CodeForm extends IntroBackground {
    private JPanel rootPanel;
    private JTextPane textPane;
    private JPanel codePanel;
    private JPanel codeIcon;
    private JPasswordField codePasswordField;
    private JButton nextButton;
    private JLabel numberLabel;
    private JPanel logoPanel;

    {
        Helper.clearBoth(codePasswordField);
        Helper.adjustTextPane(textPane);

        numberLabel.setFont(Fonts.getRegularFont().deriveFont(Font.PLAIN, 32));
        textPane.setFont(Fonts.getRegularFont().deriveFont(Font.PLAIN, 15));
        nextButton.setFont(Fonts.getRegularFont().deriveFont(Font.PLAIN, 32));
    }

    private void createUIComponents() {
        rootPanel = this;
        logoPanel = new ImagePanel(Images.getMiniLogo());
        nextButton = new BlueButton();
        codeIcon = new ImagePanel(Images.getLockIcon());
    }

    public void setPhoneLabelText(String text) {
        numberLabel.setText(text);
    }

    public String getPhoneLabelText() {
        return numberLabel.getText();
    }

    public void addActionListenerForConfirm(ActionListener actionListener) {
        nextButton.addActionListener(actionListener);
        codePasswordField.addActionListener(actionListener);
    }

    public void removeActionListenerForConfirm(ActionListener actionListener) {
        nextButton.removeActionListener(actionListener);
        codePasswordField.removeActionListener(actionListener);
    }

    public void transferFocusTo() {
        codePasswordField.requestFocusInWindow();
    }

    public String getCode() {
        return new String(this.codePasswordField.getPassword());
    }

    public void clear() {
        codePasswordField.setText("");
    }
}
