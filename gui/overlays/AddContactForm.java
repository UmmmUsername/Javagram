package gui.overlays;

import components.BlueButton;
import components.HintTextField;
import components.ImageButton;
import components.OverlayBackground;
import gui.Helper;
import gui.resources.Fonts;
import gui.resources.Images;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class AddContactForm extends OverlayBackground {
    private JPanel rootPanel;
    private JTextField firstNameTextField;
    private JTextPane textPane;
    private JButton backButton;
    private JButton applyButton;
    private JTextField lastNameTextField;
    private JFormattedTextField phoneTextField;

    {
        Helper.clearBorder(firstNameTextField);
        Helper.clearBorder(lastNameTextField);
        Helper.clearBorder(phoneTextField);

        Helper.adjustTextPane(textPane);

        textPane.setFont(Fonts.getRegularFont().deriveFont(Font.PLAIN, 16));
        Font font = new Font("Arial", Font.PLAIN, 22);
        lastNameTextField.setFont(font);
        firstNameTextField.setFont(font);

        applyButton.setFont(Fonts.getRegularFont().deriveFont(Font.PLAIN, 32));

        try {
            MaskFormatter maskFormatter = new MaskFormatter("+7 (###) ###-##-##");
            maskFormatter.setPlaceholder(null);
            maskFormatter.setPlaceholderCharacter(' ');
            phoneTextField.setFormatterFactory(new DefaultFormatterFactory(maskFormatter));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void createUIComponents() {
        rootPanel = this;

        backButton = new ImageButton(Images.getBackIcon());
        applyButton = new BlueButton();

        firstNameTextField = new HintTextField("Имя");
        lastNameTextField = new HintTextField("Фамилия");
    }

    public void setContactInfo(ContactInfo info) {
        firstNameTextField.setText(info.getFirstName());
        lastNameTextField.setText(info.getLastName());
        phoneTextField.setText(info.getPhone());
    }

    public ContactInfo getContactInfo() {
        return new ContactInfo(phoneTextField.getValue().toString(),
                firstNameTextField.getText().trim(),
                lastNameTextField.getText().trim());
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void removeBackButtonListener(ActionListener listener) {
        backButton.removeActionListener(listener);
    }

    public void addApplyButtonListener(ActionListener listener) {
        applyButton.addActionListener(listener);
    }

    public void removeApplyButtonListener(ActionListener listener) {
        applyButton.removeActionListener(listener);
    }
}
