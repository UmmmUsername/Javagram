package gui.intro;

import components.BlueButton;
import components.ImagePanel;
import gui.Helper;
import gui.resources.Fonts;
import gui.resources.Images;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class PhoneForm extends IntroBackground {
    private JPanel rootPanel;
    private JPanel logoPanel;
    private JTextPane hintTextPane;
    private JPanel phonePanel;
    private JPanel phoneIcon;
    private JFormattedTextField phoneTextField;
    private JButton nextButton;

    {
        Helper.adjustTextPane(hintTextPane);
        Helper.clearBoth(phoneTextField);


        try {
            MaskFormatter maskFormatter = new MaskFormatter("+7 (###) ###-##-##");
            maskFormatter.setPlaceholder(null);
            maskFormatter.setPlaceholderCharacter(' ');
            phoneTextField.setFormatterFactory(new DefaultFormatterFactory(maskFormatter));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.logoPanel.setBorder(BorderFactory.createEmptyBorder());
        hintTextPane.setFont(Fonts.getRegularFont().deriveFont(Font.PLAIN, 18));
        nextButton.setFont(Fonts.getRegularFont().deriveFont(Font.PLAIN, 32));
    }

    private void createUIComponents() {
        rootPanel = this;
        logoPanel = new ImagePanel(Images.getLogo(), false, true, 0);
        phoneIcon = new ImagePanel(Images.getPhoneIcon(), false, true, 0);
        nextButton = new BlueButton();
    }

    public String getPhoneNumber() {
        try {
            phoneTextField.commitEdit();
            return phoneTextField.getValue().toString();
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }

    public void addActionListenerForConfirm(ActionListener actionListener) {
        nextButton.addActionListener(actionListener);
        phoneTextField.addActionListener(actionListener);
    }

    public void removeActionListenerForConfirm(ActionListener actionListener) {
        nextButton.removeActionListener(actionListener);
        phoneTextField.removeActionListener(actionListener);
    }

    public void transferFocusTo() {
        phoneTextField.requestFocusInWindow();
    }

    public void clear() {
        phoneTextField.setText("");
        phoneTextField.setValue("");
    }
}
