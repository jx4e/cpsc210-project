package model.user;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestAthlete {
    private Athlete testAthlete1;
    
    @BeforeEach
    void runBefore() {
        testAthlete1 = new Athlete(UUID.fromString("11111111-1111-1111-1111-111111111111"),
        "Athlete1", LocalDate.of(2005, 8, 24));
    }

    @Test
    void testConstructor1() {
        assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), testAthlete1.getUuid());
        assertEquals("Athlete1", testAthlete1.getName());
        assertEquals(LocalDate.of(2005, 8, 24), testAthlete1.getDateOfBirth());
    }

    @Test
    void testConstructor2() {
        Athlete athlete = new Athlete("Athlete", LocalDate.of(2000, 1, 25));

        assertNotNull(athlete.getUuid()); // dont know how else to check because its a random uuid
        assertEquals("Athlete", athlete.getName());
        assertEquals(LocalDate.of(2000, 1, 25), athlete.getDateOfBirth());
    }

    @Test
    void testSerialize() {
        JSONObject serialized = 
                new JSONObject(
                    "{\"dob\":\"2005-08-24\",\"name\":\"Athlete1\",\"uuid\":\"11111111-1111-1111-1111-111111111111\"}");

        assertEquals(serialized.toMap(), testAthlete1.serialize().toMap());
    }

    @Test
    void testDeserialize() {
        Athlete deserialized = Athlete.deserialize(testAthlete1.serialize());

        assertEquals(testAthlete1.getName(), deserialized.getName());
        assertEquals(testAthlete1.getDateOfBirth(), deserialized.getDateOfBirth());
        assertEquals(testAthlete1.getUuid(), deserialized.getUuid());
    }
}
