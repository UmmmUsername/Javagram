package gui.intro;

import components.BlueButton;
import components.HintTextField;
import components.ImagePanel;
import gui.Helper;
import gui.resources.Fonts;
import gui.resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Registration extends IntroBackground {
    private JPanel rootPanel;
    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private JButton nextButton;
    private JTextPane textPane;
    private JPanel logoPanel;

    {
        Helper.adjustTextPane(textPane);

        Helper.clearBoth(firstNameTextField);
        Helper.clearBoth(lastNameTextField);

        textPane.setFont(Fonts.getRegularFont().deriveFont(Font.PLAIN, 18));
        firstNameTextField.setFont(Fonts.getRegularFont().deriveFont(Font.PLAIN, 28));
        lastNameTextField.setFont(Fonts.getRegularFont().deriveFont(Font.PLAIN, 28));
        nextButton.setFont(Fonts.getRegularFont().deriveFont(Font.PLAIN, 32));
    }

    private void createUIComponents() {
        rootPanel = this;
        nextButton = new BlueButton();
        logoPanel = new ImagePanel(Images.getMiniLogo(), false, true, 0);

        firstNameTextField = new HintTextField("Имя");
        lastNameTextField = new HintTextField("Фамилия");
    }

    public void clear() {
        firstNameTextField.setText("");
        lastNameTextField.setText("");
    }

    public void addActionListenerForConfirm(ActionListener actionListener) {
        nextButton.addActionListener(actionListener);
    }

    public void removeActionListenerForConfirm(ActionListener actionListener) {
        nextButton.removeActionListener(actionListener);
    }

    public String getFirstName() {
        return firstNameTextField.getText();
    }

    public String getLastNameText() {
        return lastNameTextField.getText();
    }
}
