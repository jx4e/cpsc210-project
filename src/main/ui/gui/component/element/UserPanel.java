package ui.gui.component.element;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.user.Athlete;
import model.user.User;
import ui.Main;
import ui.gui.util.Theme;

public class UserPanel extends JPanel {
    private User user;
    private String imageUrl;
    private JLabel userIconLabel;
    private JPanel infoLabel;

    public UserPanel(Theme theme, User user, String imageUrl) {
        this.user = user;
        this.imageUrl = imageUrl;

        setLayout(new GridLayout(1, 2));
        setBorder(BorderFactory.createLineBorder(theme.getSurface()));

        initIconLabel();
        initNameLabel();
    }

    /*
     * MODIFIES: this
     * EFFECTS: initialise icon label
     */
    private void initIconLabel() {
        try {
            Image image = ImageIO.read(new URI(imageUrl).toURL()).getScaledInstance(100, 100, Image.SCALE_DEFAULT);

            ImageIcon original = new ImageIcon(image);
            
            userIconLabel = new JLabel(original);
            
            userIconLabel.setPreferredSize(new Dimension(100, 100));
        } catch (Exception e) {
            e.printStackTrace();

            add(new JLabel("Invalid image."));

            return;
        }

        add(userIconLabel);
    }

    /*
     * MODIFIES: this
     * EFFECTS: initialise icon label
     */
    private void initNameLabel() {
        infoLabel = new JPanel(new GridLayout(4, 1));

        JLabel nameLabel = new JLabel(user.getName());
        infoLabel.add(nameLabel);

        JLabel type = new JLabel();

        if (user instanceof Athlete) {
            type.setText("Athlete");
        } else if (user instanceof Athlete) {
            type.setText("Coach");
        } else {
            type.setText("Unknown");
        }

        infoLabel.add(type);

        JLabel uuidLabel = new JLabel(user.getUuid().toString());
        infoLabel.add(uuidLabel);

        JLabel dobLabel = new JLabel(user.getDateOfBirth().format(Main.DATE_FORMAT));
        infoLabel.add(dobLabel);
        
        add(infoLabel);
    }
}
