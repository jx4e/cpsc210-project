package model.session;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class WeeklyScheduleTest {
    private SessionTemplate template1;
    private SessionTemplate template2;

    private WeeklySchedule testSchedule;

    @BeforeEach
    void runBefore() {
        template1 = new SessionTemplate(LocalTime.of(12, 0), LocalTime.of(14, 0), "Session1");
        template2 = new SessionTemplate(LocalTime.of(18, 0), LocalTime.of(19, 0), "Session2");

        testSchedule = new WeeklySchedule();

        testSchedule.addSessionTemplate(DayOfWeek.MONDAY, template1);
        testSchedule.addSessionTemplate(DayOfWeek.TUESDAY, template2);
        testSchedule.addSessionTemplate(DayOfWeek.WEDNESDAY, template1);
        testSchedule.addSessionTemplate(DayOfWeek.WEDNESDAY, template2);
    }

    @Test
    void testConstructor() {
        testSchedule = new WeeklySchedule();

        assertTrue(testSchedule.getWeeklyTemplate().isEmpty());
    }

    @Test
    void testAddSessionTemplate() {
        testSchedule = new WeeklySchedule();

        assertTrue(testSchedule.getWeeklyTemplate().isEmpty());

        testSchedule.addSessionTemplate(DayOfWeek.MONDAY, template1);
        testSchedule.addSessionTemplate(DayOfWeek.TUESDAY, template2);
        testSchedule.addSessionTemplate(DayOfWeek.WEDNESDAY, template1);
        testSchedule.addSessionTemplate(DayOfWeek.WEDNESDAY, template2);

        assertEquals(new ArrayList<>(Arrays.asList(template1)), testSchedule.getWeeklyTemplate().get(DayOfWeek.MONDAY));
        assertEquals(
                    new ArrayList<>(Arrays.asList(template2)), testSchedule.getWeeklyTemplate().get(DayOfWeek.TUESDAY));
        assertEquals(
                    new ArrayList<>(Arrays.asList(template1, template2)),
                            testSchedule.getWeeklyTemplate().get(DayOfWeek.WEDNESDAY));
        assertNull(testSchedule.getWeeklyTemplate().get(DayOfWeek.THURSDAY));
        assertNull(testSchedule.getWeeklyTemplate().get(DayOfWeek.FRIDAY));
        assertNull(testSchedule.getWeeklyTemplate().get(DayOfWeek.SATURDAY));
        assertNull(testSchedule.getWeeklyTemplate().get(DayOfWeek.SUNDAY));
    }

    @Test
    void testGetSessionTemplatesOnDay() {
        assertEquals(new ArrayList<>(Arrays.asList(template1)),
                     testSchedule.getSessionTemplatesOnDay(DayOfWeek.MONDAY));
        assertEquals(new ArrayList<>(Arrays.asList(template2)), 
                    testSchedule.getSessionTemplatesOnDay(DayOfWeek.TUESDAY));
        assertEquals(new ArrayList<>(Arrays.asList(template1, template2)), 
                    testSchedule.getSessionTemplatesOnDay(DayOfWeek.WEDNESDAY));
        assertEquals(new ArrayList<>(), testSchedule.getSessionTemplatesOnDay(DayOfWeek.THURSDAY));
        assertEquals(new ArrayList<>(), testSchedule.getSessionTemplatesOnDay(DayOfWeek.FRIDAY));
        assertEquals(new ArrayList<>(), testSchedule.getSessionTemplatesOnDay(DayOfWeek.SATURDAY));
        assertEquals(new ArrayList<>(), testSchedule.getSessionTemplatesOnDay(DayOfWeek.SUNDAY));
    }

    @Test
    @SuppressWarnings("methodlength")
    void testGetSessionsOnDate() {
        LocalDate tuesOct1 = LocalDate.of(2024, 10, 1); // TUES OCT 1 2024
        Session session = new Session(tuesOct1, LocalTime.of(18, 0), LocalTime.of(19, 0), "Session2");
        ArrayList<Session> producedSessions = testSchedule.getSessionsOnDate(tuesOct1);

        assertEquals(1, producedSessions.size());
        assertEquals(session.getDate(), producedSessions.get(0).getDate());
        assertEquals(session.getStartTime(), producedSessions.get(0).getStartTime());
        assertEquals(session.getEndTime(), producedSessions.get(0).getEndTime());
        assertEquals(session.getDescription(), producedSessions.get(0).getDescription());

        LocalDate wedOct2 = LocalDate.of(2024, 10, 2); // WED OCT 1 2024
        Session session1 = new Session(wedOct2, LocalTime.of(12, 0), LocalTime.of(14, 0), "Session1");
        Session session2 = new Session(wedOct2, LocalTime.of(18, 0), LocalTime.of(19, 0), "Session2");
        producedSessions = testSchedule.getSessionsOnDate(wedOct2);

        assertEquals(2, producedSessions.size());
        assertEquals(session1.getDate(), producedSessions.get(0).getDate());
        assertEquals(session1.getStartTime(), producedSessions.get(0).getStartTime());
        assertEquals(session1.getEndTime(), producedSessions.get(0).getEndTime());
        assertEquals(session1.getDescription(), producedSessions.get(0).getDescription());
        assertEquals(session2.getDate(), producedSessions.get(1).getDate());
        assertEquals(session2.getStartTime(), producedSessions.get(1).getStartTime());
        assertEquals(session2.getEndTime(), producedSessions.get(1).getEndTime());
        assertEquals(session2.getDescription(), producedSessions.get(1).getDescription());

        LocalDate thuOct3 = LocalDate.of(2024, 10, 3); // THUR OCT 1 2024
        producedSessions = testSchedule.getSessionsOnDate(thuOct3);

        assertTrue(producedSessions.isEmpty());
    }

    @Test
    void testSerialize() {
        JSONObject serialized = 
                new JSONObject(
                    "{\"WEDNESDAY\":[{\"start\":\"12:00\",\"description\":\"Session1\",\"end\":\"14:00\"},"
                    + "{\"start\":\"18:00\",\"description\":\"Session2\",\"end\":\"19:00\"}],"
                    + "\"MONDAY\":[{\"start\":\"12:00\",\"description\":\"Session1\",\"end\":\"14:00\"}],"
                    + "\"TUESDAY\":[{\"start\":\"18:00\",\"description\":\"Session2\",\"end\":\"19:00\"}]}");


        assertEquals(serialized.toMap(), testSchedule.serialize().toMap());
    }

    @Test
    void testDeserialize() {
        assertEquals(testSchedule.getWeeklyTemplate(),
                 WeeklySchedule.deserialize(testSchedule.serialize()).getWeeklyTemplate());
    }
}
