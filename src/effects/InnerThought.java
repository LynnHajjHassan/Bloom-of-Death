package effects;

import javax.swing.*;
import java.awt.*;

public class InnerThought {
    private JFrame frame;
    private JLayeredPane layeredPane;

    public InnerThought(JFrame gameFrame) {
        this.frame = gameFrame;
        this.layeredPane = gameFrame.getLayeredPane(); // Get the main frame's overlay pane
    }

    public void showInnerThought(String thought, int x, int y) {
        JLabel thoughtLabel = new JLabel("<html><b>" + thought + "</b></html>");
        thoughtLabel.setOpaque(false); // Transparent to allow custom drawing
        thoughtLabel.setForeground(new Color(80, 50, 20)); // Dark brown text
        thoughtLabel.setFont(new Font("Serif", Font.BOLD, 14)); // Custom font
        thoughtLabel.setBorder(BorderFactory.createLineBorder(new Color(120, 80, 30), 2)); // Brown border
        thoughtLabel.setSize(250, 60);
        thoughtLabel.setLocation(x, y);

        // Add to layered pane so it appears above the scene
        layeredPane.add(thoughtLabel, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();

        // Remove the thought bubble after 3 seconds
        Timer timer = new Timer(5000, e -> {
            layeredPane.remove(thoughtLabel);
            layeredPane.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
