package gui.resources;

import components.GuiHelper;

import java.awt.image.BufferedImage;

public class Images {
    private Images() {
    }

    private static BufferedImage background;
    private static BufferedImage blueButton;
    private static BufferedImage logo;
    private static BufferedImage phoneIcon;
    private static BufferedImage miniLogo;
    private static BufferedImage microLogo;
    private static BufferedImage lockIcon;
    private static BufferedImage searchIcon;
    private static BufferedImage settingsIcon;
    private static BufferedImage pencilIcon;
    private static BufferedImage messageIcon;
    private static BufferedImage backIcon;
    private static BufferedImage plusIcon;
    private static BufferedImage trashIcon;
    private static BufferedImage minimizeButton;
    private static BufferedImage closeButton;

    private static BufferedImage smallUserImage;
    private static BufferedImage largeUserImage;

    public static BufferedImage getBackground() {
        if (background == null) {
            background = loadImage("background.png");
        }

        return background;
    }

    public static BufferedImage getBlueButton() {
        if (blueButton == null) {
            blueButton = loadImage("button-background.png");
        }

        return blueButton;
    }

    public static BufferedImage getLogo() {
        if (logo == null) {
            logo = loadImage("logo.png");
        }

        return logo;
    }

    public static BufferedImage getPhoneIcon() {
        if (phoneIcon == null) {
            phoneIcon = loadImage("icon-phone.png");
        }

        return phoneIcon;
    }

    public static BufferedImage getMiniLogo() {
        if (miniLogo == null) {
            miniLogo = loadImage("logo-mini.png");
        }

        return miniLogo;
    }

    public static BufferedImage getMicroLogo() {
        if (microLogo == null) {
            microLogo = loadImage("logo-micro.png");
        }

        return microLogo;
    }

    public static BufferedImage getLockIcon() {
        if (lockIcon == null) {
            lockIcon = loadImage("icon-lock.png");
        }

        return lockIcon;
    }

    public static BufferedImage getSearchIcon() {
        if (searchIcon == null) {
            searchIcon = loadImage("icon-search.png");
        }

        return searchIcon;
    }

    public static BufferedImage getSettingsIcon() {
        if (settingsIcon == null) {
            settingsIcon = loadImage("icon-settings.png");
        }

        return settingsIcon;
    }

    public static BufferedImage getPencilIcon() {
        if (pencilIcon == null) {
            pencilIcon = loadImage("icon-edit.png");
        }

        return pencilIcon;
    }

    public static BufferedImage getMessageIcon() {
        if (messageIcon == null) {
            messageIcon = loadImage("button-send.png");
        }

        return messageIcon;
    }

    public static BufferedImage getBackIcon() {
        if (backIcon == null) {
            backIcon = loadImage("icon-back.png");
        }

        return backIcon;
    }

    public static BufferedImage getPlusIcon() {
        if (plusIcon == null) {
            plusIcon = loadImage("icon-plus.png");
        }

        return plusIcon;
    }

    public static BufferedImage getTrashIcon() {
        if (trashIcon == null) {
            trashIcon = loadImage("icon-trash-full.png");
        }

        return trashIcon;
    }

    public static BufferedImage getMinimizeButton() {
        if (minimizeButton == null) {
            minimizeButton = loadImage("icon-hide.png");
        }

        return minimizeButton;
    }

    public static BufferedImage getCloseButton() {
        if (closeButton == null) {
            closeButton = loadImage("icon-close.png");
        }

        return closeButton;
    }

    private static BufferedImage loadImage(String name) {
        return GuiHelper.loadImage("/img/" + name, Images.class);
    }

    public synchronized static BufferedImage getSmallUserImage() {
        if (smallUserImage == null)
            smallUserImage = loadImage("images (2).jpg");
        return smallUserImage;
    }

    public synchronized static BufferedImage getLargeUserImage() {
        if (largeUserImage == null)
            largeUserImage = loadImage("User-icon.png");
        return largeUserImage;
    }

    public static BufferedImage getUserImage(boolean small) {
        return small ? getSmallUserImage() : getLargeUserImage();
    }
}
