package model.user;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.session.Session;
import model.session.SessionTemplate;

public class TestSquad {
    private Squad testSquad;
    private Athlete athlete1;
    private Athlete athlete2;
    private Athlete athlete3;
    private Coach coach1;
    private Coach coach2;
    private ArrayList<Athlete> athletes;
    private ArrayList<Coach> coaches;
    private SessionTemplate template;
    
    @BeforeEach
    void runBefore() {
        athlete1 = new Athlete("A1", LocalDate.of(2005, 10, 8));
        athlete2 = new Athlete("A2", LocalDate.of(2001, 7, 5));
        athlete3 = new Athlete("A3", LocalDate.of(2000, 4, 18));

        coach1 = new Coach("C1", LocalDate.of(1980, 12, 18));
        coach2 = new Coach("C2", LocalDate.of(1980, 3, 8));

        athletes = new ArrayList<>(Arrays.asList(athlete1, athlete2, athlete3));
        coaches = new ArrayList<>(Arrays.asList(coach1, coach2));

        testSquad = new Squad(UUID.fromString("11111111-1111-1111-1111-111111111111"),
                         "UBC Varsity", athletes, coaches);

        template = new SessionTemplate(LocalTime.of(12, 0), LocalTime.of(14, 0), "Session");
    }

    @Test
    void testConstructor1() {
        assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), testSquad.getUuid());
        assertEquals("UBC Varsity", testSquad.getName());
        assertEquals(athletes, testSquad.getAthletes());
        assertEquals(coaches, testSquad.getCoaches());
    }

    @Test
    void testConstructor2() {
        Squad theSquad = new Squad("The Squad");

        assertNotNull(theSquad.getUuid());
        assertEquals("The Squad", theSquad.getName());
        assertTrue(theSquad.getAthletes().isEmpty());
        assertTrue(theSquad.getCoaches().isEmpty());
    }

    @Test
    void testConstructor3() {
        Squad theSquad = new Squad("The Squad", athletes, coaches);

        assertNotNull(theSquad.getUuid());
        assertEquals("The Squad", theSquad.getName());
        assertEquals(athletes, testSquad.getAthletes());
        assertEquals(coaches, testSquad.getCoaches());
    }


    @Test
    void testAddSessionTemplateToWeeklySchedule() {
        testSquad.addSessionTemplateToWeeklySchedule(DayOfWeek.MONDAY, template);
        testSquad.addSessionTemplateToWeeklySchedule(DayOfWeek.FRIDAY, template);

        assertEquals(new ArrayList<>(Arrays.asList(template)), 
                testSquad.getWeeklySchedule().getSessionTemplatesOnDay(DayOfWeek.MONDAY));
        assertEquals(new ArrayList<>(Arrays.asList(template)), 
                testSquad.getWeeklySchedule().getSessionTemplatesOnDay(DayOfWeek.FRIDAY));
        assertEquals(new ArrayList<>(), testSquad.getWeeklySchedule().getSessionTemplatesOnDay(DayOfWeek.WEDNESDAY));
    }

    @Test
    @SuppressWarnings("methodlength")
    void testRecordAttendance() {
        assertTrue(testSquad.getRecordedSessions().isEmpty());

        testSquad.addSessionTemplateToWeeklySchedule(DayOfWeek.MONDAY, template);
        testSquad.addSessionTemplateToWeeklySchedule(DayOfWeek.FRIDAY, template);

        LocalDate monOct7 = LocalDate.of(2024, 10, 7);
        ArrayList<Athlete> attending = new ArrayList<>(Arrays.asList(athlete1, athlete2));
        Session addedSession = new Session(monOct7, attending, template.getStartTime(), 
                    template.getEndTime(), template.getDescription());

        testSquad.recordAttendance(monOct7, template, attending);

        assertEquals(new ArrayList<>(Arrays.asList(addedSession)), testSquad.getRecordedSessions().get(monOct7));

        LocalDate tuesOct7 = LocalDate.of(2024, 10, 8);

        testSquad.recordAttendance(tuesOct7, template, attending);

        assertFalse(testSquad.getRecordedSessions().containsKey(tuesOct7));

        template = new SessionTemplate(LocalTime.of(13, 0), LocalTime.of(14, 0), "Session1");
        testSquad.addSessionTemplateToWeeklySchedule(DayOfWeek.MONDAY, template);
        Session anotherAddedSession = new Session(monOct7, attending, template.getStartTime(), 
                template.getEndTime(), template.getDescription());

        testSquad.recordAttendance(monOct7, template, attending);

        assertEquals(new ArrayList<>(Arrays.asList(addedSession, anotherAddedSession)), 
                testSquad.getRecordedSessions().get(monOct7));

        testSquad.recordAttendance(monOct7, template, new ArrayList<>(Arrays.asList(athlete3)));

        assertEquals(new ArrayList<>(Arrays.asList(addedSession, anotherAddedSession)), 
                testSquad.getRecordedSessions().get(monOct7));
        assertEquals(3, testSquad.getRecordedSessions().get(monOct7).get(1).getAttending().size());
        assertEquals(athletes, testSquad.getRecordedSessions().get(monOct7).get(1).getAttending());
    }

    @Test
    void testCalculateAttendance() {
        testSquad.addSessionTemplateToWeeklySchedule(DayOfWeek.MONDAY, template);
        testSquad.addSessionTemplateToWeeklySchedule(DayOfWeek.TUESDAY, template);
        testSquad.addSessionTemplateToWeeklySchedule(DayOfWeek.WEDNESDAY, template);
        testSquad.addSessionTemplateToWeeklySchedule(DayOfWeek.THURSDAY, template);

        LocalDate monOct7 = LocalDate.of(2024, 10, 7);
        ArrayList<Athlete> attending = new ArrayList<>(Arrays.asList(athlete1, athlete2));

        testSquad.recordAttendance(monOct7, template, new ArrayList<>());

        assertEquals(0f, testSquad.calculateAttendance(monOct7, monOct7));

        assertEquals(0f, testSquad.calculateAttendance(monOct7.plusDays(100), monOct7.plusDays(120)));

        testSquad.recordAttendance(monOct7, template, attending); 
        testSquad.recordAttendance(monOct7.plusDays(1), template, attending); 

        assertEquals(100f * 2f / 3f, testSquad.calculateAttendance(monOct7, monOct7.plusDays(1)));
    }

    @Test
    void testCalculateAttendanceForAthlete() {
        testSquad.addSessionTemplateToWeeklySchedule(DayOfWeek.MONDAY, template);
        testSquad.addSessionTemplateToWeeklySchedule(DayOfWeek.TUESDAY, template);
        testSquad.addSessionTemplateToWeeklySchedule(DayOfWeek.WEDNESDAY, template);
        testSquad.addSessionTemplateToWeeklySchedule(DayOfWeek.THURSDAY, template);

        LocalDate monOct7 = LocalDate.of(2024, 10, 7);
        ArrayList<Athlete> attending = new ArrayList<>(Arrays.asList(athlete1, athlete2));

        testSquad.recordAttendance(monOct7, template, new ArrayList<>());

        assertEquals(0f, testSquad.calculateAttendanceForAthlete(athlete1, monOct7, monOct7));

        assertEquals(0f, testSquad.calculateAttendanceForAthlete(athlete1, monOct7.plusDays(100),
                 monOct7.plusDays(120)));

        testSquad.recordAttendance(monOct7, template, attending); 
        testSquad.recordAttendance(monOct7.plusDays(1), template, attending); 

        assertEquals(100f, testSquad.calculateAttendanceForAthlete(athlete1, monOct7, monOct7.plusDays(1)));

        testSquad.recordAttendance(monOct7.plusDays(2), template, new ArrayList<>()); 

        assertEquals(100f * 2f / 3f, testSquad.calculateAttendanceForAthlete(athlete1, monOct7, monOct7.plusDays(2)));
    }

    @Test
    void testSerialize() {
        JSONObject json = new JSONObject()
                .put("uuid", testSquad.getUuid().toString())
                .put("name", testSquad.getName())
                .put("athletes", testSquad.serializeList(testSquad.getAthletes()))
                .put("coaches", testSquad.serializeList(testSquad.getCoaches()))
                .put("schedule", testSquad.getWeeklySchedule().serialize());

        assertEquals(json.toString(), testSquad.serialize().toString());
    }

    @Test 
    void testDeserialize() {
        Squad deserialized = Squad.deserialize(testSquad.serialize());

        assertEquals(testSquad.getUuid(), deserialized.getUuid());
        assertEquals(testSquad.getName(), deserialized.getName());
        assertEquals(testSquad.getWeeklySchedule().getWeeklyTemplate(),
                 deserialized.getWeeklySchedule().getWeeklyTemplate());
    }
}
