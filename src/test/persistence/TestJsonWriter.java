package persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.session.SessionTemplate;
import model.session.WeeklySchedule;
import model.user.Athlete;
import model.user.Coach;
import model.user.Squad;

public class TestJsonWriter {
    private FileManager manager;
    private JsonWriter writer;
    private JsonReader reader;
    private Squad testSquad;
    private WeeklySchedule testSchedule;
    private Athlete athlete1;
    private Athlete athlete2;
    private Athlete athlete3;
    private Coach coach1;
    private Coach coach2;
    private ArrayList<Athlete> athletes;
    private ArrayList<Coach> coaches;
    private SessionTemplate template1;
    private SessionTemplate template2;

    @BeforeEach
    void runBefore() {
        manager = new FileManager();
        writer = new JsonWriter(manager);
        reader = new JsonReader(manager);

        athlete1 = new Athlete("A1", LocalDate.of(2005, 10, 8));
        athlete2 = new Athlete("A2", LocalDate.of(2001, 7, 5));
        athlete3 = new Athlete("A3", LocalDate.of(2000, 4, 18));

        coach1 = new Coach("C1", LocalDate.of(1980, 12, 18));
        coach2 = new Coach("C2", LocalDate.of(1980, 3, 8));

        athletes = new ArrayList<>(Arrays.asList(athlete1, athlete2, athlete3));
        coaches = new ArrayList<>(Arrays.asList(coach1, coach2));

        testSquad = new Squad(UUID.fromString("11111111-1111-1111-1111-111111111111"),
                         "UBC Varsity", athletes, coaches);
        
        template1 = new SessionTemplate(LocalTime.of(12, 0), LocalTime.of(14, 0), "Session1");
        template2 = new SessionTemplate(LocalTime.of(18, 0), LocalTime.of(19, 0), "Session2");

        testSchedule = new WeeklySchedule();

        testSchedule.addSessionTemplate(DayOfWeek.MONDAY, template1);
        testSchedule.addSessionTemplate(DayOfWeek.TUESDAY, template2);
        testSchedule.addSessionTemplate(DayOfWeek.WEDNESDAY, template1);
        testSchedule.addSessionTemplate(DayOfWeek.WEDNESDAY, template2);
    }

    @Test
    void testWriteSquad() {
        try {
            writer.write(testSquad, "testsquad");

            Squad deserialized = reader.deserializeSquad("testsquad");

            assertEquals(testSquad.getUuid(), deserialized.getUuid());
            assertEquals(testSquad.getName(), deserialized.getName());
            assertEquals(testSquad.getWeeklySchedule().getWeeklyTemplate(),
                     deserialized.getWeeklySchedule().getWeeklyTemplate());
        } catch (Exception e) {
            fail();
        }
        
    }

    @Test
    void testWriteSchedule() {
        try {
            writer.write(testSchedule, "testschedule");
            assertEquals(testSchedule.getWeeklyTemplate(),
                     reader.deserializeSchedule("testschedule").getWeeklyTemplate());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testWriteOther() {
        try {
            assertFalse(writer.write(new Athlete("A", LocalDate.of(2020, 10, 20)), "a"));
        } catch (Exception e) {
            fail();
        }
    }
}
