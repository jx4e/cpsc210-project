package model.user;

import java.time.LocalDate;
import java.util.UUID;

// Represents a user with a UUID, name, dateOfBirth 
public abstract class User {
    private final UUID uuid;
    private final String name;
    private final LocalDate dateOfBirth;

    /*
     * REQUIRES: dateOfBirth is <= today
     * EFFECTS: uuid of user is set to uuid;
     *          name of user is set to name;
     *          dateOfBirth of user set to dateOfBirth;
     */
    public User(UUID uuid, String name, LocalDate dateOfBirth) {
        this.uuid = uuid;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    /*
     * EFFECTS: uuid of user is set to a random new UUID;
     *          name of user is set to name;
     *          dateOfBirth of user set to dateOfBirth;
     */
    public User(String name, LocalDate dateOfBirth) {
        this(UUID.randomUUID(), name, dateOfBirth);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return uuid.equals(((User) obj).getUuid());
        }

        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth; 
    }
}
