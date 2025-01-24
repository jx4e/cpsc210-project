package model.user;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestCoach {
    private Coach testCoach1;
    
    @BeforeEach
    void runBefore() {
        testCoach1 = new Coach(UUID.fromString("11111111-1111-1111-1111-111111111111"),
        "Coach1", LocalDate.of(1997, 7, 10));
    }

    @Test
    void testConstructor1() {
        assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), testCoach1.getUuid());
        assertEquals("Coach1", testCoach1.getName());
        assertEquals(LocalDate.of(1997, 7, 10), testCoach1.getDateOfBirth());
    }

    @Test
    void testConstructor2() {
        Coach coach = new Coach("Athlete", LocalDate.of(2000, 1, 25));

        assertNotNull(coach.getUuid()); // dont know how else to check because its a random uuid
        assertEquals("Athlete", coach.getName());
        assertEquals(LocalDate.of(2000, 1, 25), coach.getDateOfBirth());
    }

    @Test
    void testSerialize() {
        JSONObject serialized = 
                new JSONObject(
                    "{\"dob\":\"1997-07-10\", \"name\":\"Coach1\", \"uuid\":\"11111111-1111-1111-1111-111111111111\"}");

        assertEquals(serialized.toMap(), testCoach1.serialize().toMap());
    }

    @Test
    void testDeserialize() {
        Athlete deserialized = Athlete.deserialize(testCoach1.serialize());

        assertEquals(testCoach1.getName(), deserialized.getName());
        assertEquals(testCoach1.getDateOfBirth(), deserialized.getDateOfBirth());
        assertEquals(testCoach1.getUuid(), deserialized.getUuid());
    }
}
