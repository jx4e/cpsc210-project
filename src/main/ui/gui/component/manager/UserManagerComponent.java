package ui.gui.component.manager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import model.DataManager;
import model.user.Athlete;
import model.user.Coach;
import model.user.User;
import ui.gui.component.StackableComponent;
import ui.gui.component.element.DateTextField;
import ui.gui.component.element.PromptTextField;
import ui.gui.component.element.UserPanel;
import ui.gui.util.ResizeHelper;
import ui.gui.util.SetStack;
import ui.gui.util.Theme;

// Represents a jcomponent to do user management functions in the GUI
public class UserManagerComponent extends JPanel implements ResizeHelper, StackableComponent {
    private DataManager dataManager;

    private Theme theme;
    private JFrame frame;
    private JPanel menu;
    private JPanel createUserPanel;
    private JScrollPane userListPane;
    private JPanel userListPanel;

    private Stack<JComponent> componentStack;

    /*
     * EFFECTS: create new UserManagerComponent
     */
    public UserManagerComponent(JFrame frame, DataManager dataManager, Theme theme) {
        this.frame = frame;
        this.dataManager = dataManager;
        this.theme = theme;

        componentStack = new SetStack<>();

        init();
    }

    /*
     * MODIFIES: this
     * EFFECTSL initialise the panels
     */
    private void init() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(theme.getBackground());
        
        initMenu();
        initCreateUserPanel();
        initUserList();
        
        pushComponent(menu);
    }

    /*
     * MODIFIES: this
     * EFFECTS: initialises the menu
     */
    private void initMenu() {
        menu = new JPanel(new BorderLayout());
        menu.setBorder(new EmptyBorder(10, 10, 10, 10));
        menu.setBackground(theme.getBackground());
        JPanel inner = new JPanel(new GridLayout(4, 1));
        inner.setBackground(theme.getBackground());

        JButton userManagerButton = new JButton("Create User");
        userManagerButton.setBackground(Color.BLUE);
        userManagerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pushComponent(createUserPanel);
            }
        });
        inner.add(userManagerButton);

        JButton userListButton = new JButton("View Users");
        userListButton.setBackground(Color.BLUE);
        userListButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadUserPanels();
                pushComponent(userListPane);
            }
        });
        inner.add(userListButton);


        menu.add(inner);
    }

    /*
     * MODIFIES: this
     * EFFECTTS: initialise create user panel
     */
    @SuppressWarnings("methodlength")
    private void initCreateUserPanel() {
        createUserPanel = new JPanel(new GridLayout(5, 1));
        createUserPanel.setBackground(theme.getBackground());

        JLabel titleLabel = new JLabel("Create User");
        titleLabel.setForeground(theme.getText());
        Runnable resizeAction = () -> titleLabel.setFont(theme.getFont().deriveFont(12f));
        resizeAction.run();
        onResize(titleLabel, resizeAction);
        createUserPanel.add(titleLabel);  

        PromptTextField nameField = new PromptTextField("Name");
        createUserPanel.add(nameField);
        
        DateTextField dateField = new DateTextField("DOB");
        createUserPanel.add(dateField);

        JComboBox<String> type = new JComboBox<>(new String[]{"Coach", "Athlete"});
        createUserPanel.add(type);

        JButton createUserButton = new JButton("Create User");
        createUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!dateField.isValidDate()) {
                    JOptionPane.showMessageDialog(frame, "Date is invalid!", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(frame, "Confirm?",
                         "Confirm", JOptionPane.YES_NO_OPTION)) {
                    return;
                }

                User user;

                if (type.getSelectedIndex() == 0) {
                    user = new Coach(nameField.getText(), dateField.getDate());
                } else {
                    user = new Athlete(nameField.getText(), dateField.getDate());
                }

                nameField.reset();
                dateField.reset();

                dataManager.loadUser(user);

                JOptionPane.showMessageDialog(frame, "Created new user: " + user.getName());

                popComponent();
            }
        });
        createUserPanel.add(createUserButton);
    }

    /*
     * MODIFIES: this
     * EFFECTS: initialises the user list pane
     */
    private void initUserList() {
        userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setBackground(theme.getBackground());

        loadUserPanels();

        userListPane = new JScrollPane(userListPanel);
        userListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds userpanels to userlistpanel
     */
    private void loadUserPanels() {
        userListPanel.removeAll();

        for (User user : dataManager.getUsers()) {

            String imgName = user.getName().replace(" ", "_");

            String url = "https://gothunderbirds.ca/images/2024/10/9/" + imgName + ".jpg?width=300";

            UserPanel userPanel = new UserPanel(theme, user, url);
            userListPanel.add(userPanel);
        }
    }

    @Override
    public int getFrameWidth() {
        return frame.getWidth();
    }

    @Override
    public int getFrameHeight() {
        return frame.getHeight();
    }

    @Override
    public Stack<JComponent> componentStack() {
        return componentStack;
    }

    @Override
    public void removeFromParent(JComponent component) {
        remove(component);
    }

    @Override
    public void addToParent(JComponent component) {
        add(component);
    }

    @Override
    public void repaintParent() {
        repaint();
    }

    @Override
    public void revalidateParent() {
        revalidate();
    }
}
