package gui;

import components.GuiHelper;
import gui.overlays.ContactInfo;
import gui.resources.Images;
import org.javagram.dao.KnownPerson;
import org.javagram.dao.Person;
import org.javagram.dao.proxy.TelegramProxy;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Helper {
    private Helper() {
    }

    public static void adjustTextPane(JTextPane textPane) {
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_CENTER);
        textPane.setParagraphAttributes(attributes, false);
        clearBoth(textPane);
    }

    public static void clearBoth(JComponent textPane) { // removeRedundant
        clearBackground(textPane);
        clearBorder(textPane);
    }

    public static void clearBackground(JComponent component) {
        component.setOpaque(false);
        component.setBackground(new Color(0, 0, 0, 0));

    }

    public static void clearBorder(JComponent component) {
        component.setBorder(BorderFactory.createEmptyBorder());
    }

    public static BufferedImage getPhoto(TelegramProxy telegramProxy, Person person, boolean small) {
        BufferedImage image;

        try {
            image = telegramProxy.getPhoto(person, small);
        } catch (Exception e) {
            e.printStackTrace();
            image = null;
        }

        if (image == null)
            image = Images.getUserImage(small);
        return image;
    }

    public static BufferedImage getPhoto(TelegramProxy telegramProxy, Person person, boolean small, boolean circle) {
        BufferedImage photo = getPhoto(telegramProxy, person, small);
        if (circle)
            photo = GuiHelper.makeCircle(photo);
        return photo;
    }

    public static ContactInfo toContactInfo(KnownPerson person, TelegramProxy proxy, boolean small, boolean makeCircle) {
        ContactInfo info = toContactInfo(person);
        if (proxy != null)
            info.setPhoto(getPhoto(proxy, person, small, makeCircle));
        return info;
    }

    public static ContactInfo toContactInfo(KnownPerson person) {
        return new ContactInfo(person.getPhoneNumber(), person.getFirstName(), person.getLastName(), person.getId());
    }
}
