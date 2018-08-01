package gui.main;

import components.GuiHelper;
import components.HintTextField;
import components.ImageButton;
import components.ImagePanel;
import gui.Helper;
import gui.resources.Fonts;
import gui.resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class MainForm extends JPanel {
    private JPanel rootPanel;
    private JPanel logoPanel;
    private JButton settingsButton;
    private JTextField searchTextField;
    private JPanel contactsPanel;
    private JButton editButton;
    private JButton sendButton;
    private JPanel searchIcon;
    private JPanel messagesPanel;
    private JPanel titlePanel;
    private JScrollPane messageTextScrollPane;
    private JPanel meDisplayingPanel;
    private JPanel buddyPanel;
    private JTextArea messageTextArea;

    private String meText;
    private BufferedImage mePhoto;

    private String buddyText;
    private BufferedImage buddyPhoto;

    public MainForm() {
        contactsPanel.add(new JPanel());
        messagesPanel.add(new JPanel());

        GuiHelper.decorateScrollPane(messageTextScrollPane);

        Helper.clearBorder(searchTextField);

        messageTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
    }

    private void createUIComponents() {
        rootPanel = this;
        logoPanel = new ImagePanel(Images.getMicroLogo());
        searchIcon = new ImagePanel(Images.getSearchIcon());
        settingsButton = new ImageButton(Images.getSettingsIcon());
        editButton = new ImageButton(Images.getPencilIcon());
        sendButton = new ImageButton(Images.getMessageIcon());

        meDisplayingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int leftMostPoint = settingsButton.getX();

                if (meText != null) {
                    int inset = 10;
                    Font font = new Font ("Arial", Font.BOLD, 20);
                    Color color = Color.white;
                    String text = meText;

                    leftMostPoint = GuiHelper.drawText(g, text, color, font, 0, 0, leftMostPoint, this.getHeight(), inset, true);
                }

                if (mePhoto != null) {
                    int inset = 2;
                    BufferedImage image = mePhoto;

                    GuiHelper.drawImage(g, image, 0, 0, leftMostPoint, this.getHeight(), inset, true);
                }
            }
        };

        buddyPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);

                int leftMostPoint = buddyPanel.getWidth();
                int rightMostPoint = 2;

                if (buddyPhoto != null) {
                    int inset = 2;
                    BufferedImage image = buddyPhoto;

                    rightMostPoint = GuiHelper.drawImage(graphics, image, rightMostPoint, 0, leftMostPoint - rightMostPoint, this.getHeight(), inset, false);
                }

                if (buddyText != null) {
                    int inset = 10;
                    Font font = new Font("Arial", Font.PLAIN, 18);
                    Color color = Color.black;
                    String text = buddyText;

                    GuiHelper.drawText(graphics, text, color, font, rightMostPoint, 0, leftMostPoint - rightMostPoint, this.getHeight(), inset, false);
                }
            }
        };

        searchTextField = new HintTextField("Поиск");
    }

    public Component getContactsPanel() {
        return this.contactsPanel.getComponent(0);
    }

    public void setContactsPanel(Component contactsPanel) {
        this.contactsPanel.removeAll();
        this.contactsPanel.add(contactsPanel);
    }

    public String getMeText() {
        return meText;
    }

    public void setMeText(String meText) {
        if (!Objects.equals(this.meText, meText)) {
            this.meText = meText;
            repaint();
        }
    }

    public BufferedImage getMePhoto() {
        return mePhoto;
    }

    public void setMePhoto(BufferedImage mePhoto) {
        this.mePhoto = mePhoto;
        repaint();
    }

    public String getBuddyText() {
        return buddyText;
    }

    public void setBuddyText(String buddyText) {
        if (!Objects.equals(this.buddyText, buddyText)) {
            this.buddyText = buddyText;
            repaint();
        }
    }

    public BufferedImage getBuddyPhoto() {
        return buddyPhoto;
    }

    public void setBuddyPhoto(BufferedImage buddyPhoto) {
        this.buddyPhoto = buddyPhoto;
        repaint();
    }

    public String getMessageText() {
        return this.messageTextArea.getText();
    }

    public void setMessageText(String text) {
        this.messageTextArea.setText(text);
    }

    public Component getMessagesPanel() {
        return this.messagesPanel.getComponent(0);
    }

    public void setMessagesPanel(Component messagesPanel) {
        this.messagesPanel.removeAll();
        this.messagesPanel.add(messagesPanel);
    }

    public void clearFields() {
        searchTextField.setText("");
        messageTextArea.setText("");
    }

    public void addSettingsButtonListener(ActionListener listener) {
        settingsButton.addActionListener(listener);
    }

    public void removeSettingsButtonListener(ActionListener listener) {
        settingsButton.removeActionListener(listener);
    }

    public void addSendButtonListener(ActionListener listener) {
        sendButton.addActionListener(listener);
    }

    public void removeSendButtonListener(ActionListener listener) {
        sendButton.removeActionListener(listener);
    }

    public void addEditButtonListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }

    public void removeEditButtonListener(ActionListener listener) {
        editButton.removeActionListener(listener);
    }

    public void addSearchEventListener(ActionListener listener) {
        this.searchTextField.addActionListener(listener);
    }

    public void removeSearchEventListener(ActionListener listener) {
        this.searchTextField.removeActionListener(listener);
    }

    public String getSearchText() {
        return this.searchTextField.getText();
    }
}