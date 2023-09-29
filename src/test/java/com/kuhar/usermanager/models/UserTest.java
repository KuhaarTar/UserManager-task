package com.kuhar.usermanager.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    void isBirthdayValid() {
        User user = new User("bad-email", "Jane", "Smith",
                LocalDate.of(2025, 8, 25), "456 Elm Avenue", "555-987-6543");
        assertFalse(user.isBirthdayValid());
    }

    @Test
    void isEmailValid() {
        User user = new User("bad-email", "Jane", "Smith",
                LocalDate.of(2025, 8, 25), "456 Elm Avenue", "555-987-6543");
        assertFalse(user.isEmailValid());
    }

    @Test
    void areFieldsNull() {
        User user = new User("bad-email", null, "Smith",
                LocalDate.of(2025, 8, 25), "456 Elm Avenue", "555-987-6543");
        assertTrue(user.areFieldsNull());
    }

}
