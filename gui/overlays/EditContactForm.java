package gui.overlays;

import components.*;
import gui.Helper;
import gui.resources.Fonts;
import gui.resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class EditContactForm extends OverlayBackground {
    private JPanel rootPanel;
    private JPanel contactPanel;
    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private JPanel photoPanel;
    private JLabel phoneLabel;
    private JButton deleteButton;
    private JButton saveButton;
    private JButton backButton;

    private int id;

    private final static String phoneRegexFrom = "^\\+?(\\d*)(\\d{3})(\\d{3})(\\d{2})(\\d{2})$", phoneRegexTo = "+$1 ($2) $3-$4-$5";


    {
        Helper.clearBoth(firstNameTextField);
        Helper.clearBoth(lastNameTextField);

        saveButton.setFont(Fonts.getRegularFont().deriveFont(Font.PLAIN, 32));
    }

    private void createUIComponents() {
        rootPanel = this;

        deleteButton = new ImageAndTextButton(Images.getTrashIcon(), true, 20);
        saveButton = new BlueButton();
        backButton = new ImageButton(Images.getBackIcon());

        photoPanel = new ImagePanel(null, true, false, 0);
        firstNameTextField = new HintTextField("Имя");
        lastNameTextField = new HintTextField("Фамилия");
    }

    public void setContactInfo(ContactInfo info) {
        firstNameTextField.setText(info.getFirstName());
        lastNameTextField.setText(info.getLastName());
        phoneLabel.setText(info.getPhone().replaceAll(phoneRegexFrom, phoneRegexTo));
        ((ImagePanel) photoPanel).setImage(info.getPhoto());
        id = info.getId();
    }

    public ContactInfo getContactInfo() {
        ContactInfo info = new ContactInfo();

        info.setPhone(phoneLabel.getText().trim().replaceAll("\\D+", ""));
        info.setFirstName(firstNameTextField.getText().trim());
        info.setLastName(lastNameTextField.getText().trim());
        info.setPhoto((BufferedImage) ((ImagePanel) photoPanel).getImage());
        info.setId(id);

        return info;
    }

    public void addActionListenerForSave(ActionListener actionListener) {
        saveButton.addActionListener(actionListener);
    }

    public void removeActionListenerForSave(ActionListener actionListener) {
        saveButton.removeActionListener(actionListener);
    }

    public void addActionListenerForDelete(ActionListener actionListener) {
        deleteButton.addActionListener(actionListener);
    }

    public void removeActionListenerForDelete(ActionListener actionListener) {
        deleteButton.removeActionListener(actionListener);
    }

    public void addActionListenerForReturn(ActionListener actionListener) {
        backButton.addActionListener(actionListener);
    }

    public void removeActionListenerForReturn(ActionListener actionListener) {
        backButton.removeActionListener(actionListener);
    }
}
