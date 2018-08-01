package gui.resources;

import java.awt.*;
import java.io.InputStream;

public class Fonts {
    private Fonts() {
    }

    private static Font light;
    private static Font regular;
    private static Font semiBold;

    public static Font getLightFont() {
        if (light == null)
            light = loadFont("OpenSansLight.ttf");
        return light;
    }

    public static Font getRegularFont() {
        if (regular == null)
            regular = loadFont("OpenSansRegular.ttf");
        return regular;
    }

    public static Font getSemiBoldFont() {
        if (semiBold == null)
            semiBold = loadFont("OpenSansSemiBold.ttf");
        return semiBold;
    }

    private static Font loadFont(String name) {
        try(InputStream inputStream = Fonts.class.getResourceAsStream("/fonts/" + name)) {
            return Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("serif", Font.PLAIN, 24);
        }
    }
}
