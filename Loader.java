import gui.frame.MainFrame;
import org.javagram.dao.ApiBridgeTelegramDAO;
import org.javagram.dao.DebugTelegramDAO;
import org.javagram.dao.TelegramDAO;

import javax.swing.*;

public class Loader {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
//                    for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                        if(info.getName().equals("Nimbus")) {
//                            UIManager.setLookAndFeel(info.getClassName());
//                            break;
//                        }
//                    }
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    TelegramDAO telegramDAO = //new ApiBridgeTelegramDAO(Config.SERVER, Config.APP_ID, Config.APP_HASH);
                        new DebugTelegramDAO();
                    MainFrame frame = new MainFrame(telegramDAO);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        });
    }
}
