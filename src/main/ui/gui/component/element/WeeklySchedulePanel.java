package ui.gui.component.element;

import java.awt.GridLayout;
import java.time.DayOfWeek;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.session.SessionTemplate;
import model.session.WeeklySchedule;
import ui.gui.util.Theme;

// Represents a panel which displays a weekly schedule
public class WeeklySchedulePanel extends JPanel {
    private Theme theme;
    private WeeklySchedule schedule;

    public WeeklySchedulePanel(Theme theme, WeeklySchedule schedule) {
        this.theme = theme;
        this.schedule = schedule;

        setLayout(new GridLayout(7, 1));

        for (DayOfWeek day : DayOfWeek.values()) {
            add(createDailyPanel(day));
        }
    }

    /*
     * EFFECTS: produces jpanel of sessions on day day
     */
    private JPanel createDailyPanel(DayOfWeek day) {
        ArrayList<SessionTemplate> sessions = schedule.getSessionTemplatesOnDay(day);

        int size = sessions.isEmpty() ? 2 : sessions.size() + 1;

        JPanel panel = new JPanel(new GridLayout(size, 1));
        panel.setBorder(BorderFactory.createLineBorder(theme.getSurface()));

        JLabel dayLabel = new JLabel(day.toString());
        panel.add(dayLabel);
        
        if (sessions.isEmpty()) {
            JLabel noSessionLabel = new JLabel("No Sessions On This Day");
            panel.add(noSessionLabel);
        } else {
            for (SessionTemplate session : sessions) {
                JPanel sessionPanel = new JPanel(new GridLayout(2, 1));
                sessionPanel.setBorder(BorderFactory.createLineBorder(theme.getAccent()));
    
                JLabel timeLabel = new JLabel(session.getStartTime() + " - " + session.getEndTime());
                sessionPanel.add(timeLabel);
    
                JLabel descriptionLabel = new JLabel(session.getDescription());
                sessionPanel.add(descriptionLabel);
    
                panel.add(sessionPanel);
            }
        }

        return panel;
    }
}
