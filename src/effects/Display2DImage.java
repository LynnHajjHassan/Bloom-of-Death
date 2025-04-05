package effects;

import javax.swing.*;
import java.awt.*;

public class Display2DImage {
    private JLayeredPane layeredPane;

    public Display2DImage(JFrame gameFrame) {
        this.layeredPane = gameFrame.getLayeredPane();
    }

    public void showImage(String imagePath, int x, int y, int width, int height, int duration) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setSize(width, height);
        imageLabel.setLocation(x, y);
        
        // Add to layered pane
        layeredPane.add(imageLabel, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
        
        // Remove the image after the specified duration
        Timer timer = new Timer(duration, e -> {
            layeredPane.remove(imageLabel);
            layeredPane.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
