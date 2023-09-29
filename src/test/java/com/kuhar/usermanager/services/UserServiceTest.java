package com.kuhar.usermanager.services;

import com.kuhar.usermanager.exceptions.InvalidInputException;
import com.kuhar.usermanager.exceptions.UserNotFoundException;
import com.kuhar.usermanager.models.User;
import com.kuhar.usermanager.models.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private final UserService userService;
    private User user1;
    private User user2;

    public UserServiceTest() {
        this.userService = new UserService();
    }

    @BeforeEach
    void setUp() {
        userService.getUsers().clear();
        userService.setValidAge(18);
        this.user1 = new User("user1@gmail.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main Street", "555-123-4567");
        this.user2 = new User("user2@example.com", "Jane", "Smith",
                LocalDate.of(2005, 8, 25), "456 Elm Avenue", "555-987-6543");
    }

    @Test
    void saveUser() {
        userService.saveUser(user1);
        User savedUser = userService.getUsers().get(1L);
        assertEquals(user1.getEmail(), savedUser.getEmail());
        assertEquals(user1.getFirstName(), savedUser.getFirstName());
        assertEquals(user1, savedUser);
    }

    @Test
    void saveBadEmailFormatUser() {
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            userService.saveUser(new User("bad-email", "Jane", "Smith",
                    LocalDate.of(1985, 8, 25), "456 Elm Avenue", "555-987-6543"));
        });
        String expectedMessage = "Invalid email.";
        assertTrue(expectedMessage.contains(exception.getMessage()));
    }

    @Test
    void saveBadBirthdayFormatUser() {
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            userService.saveUser(new User("user1@gmail.com", "Jane", "Smith",
                    LocalDate.of(2024, 8, 25), "456 Elm Avenue", "555-987-6543"));
        });
        String expectedMessage = "Invalid birthday date.";
        assertTrue(expectedMessage.contains(exception.getMessage()));
    }

    @Test
    void testIfUserIsNotAdult() {
        userService.getUsers().clear();
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            userService.saveUser(new User("user1@gmail.com", "Jane", "Smith",
                    LocalDate.of(2020, 8, 25), "456 Elm Avenue", "555-987-6543"));
        });
        String expectedMessage = "User must be at least 18 years old.";
        assertTrue(expectedMessage.contains(exception.getMessage()));
    }

    @Test
    void testIfSomeNullFields() {
        userService.getUsers().clear();
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            userService.saveUser(new User(null, "Jane", null,
                    LocalDate.of(2005, 8, 25), "456 Elm Avenue", null));
        });
        String expectedMessage = "Some of the fields are null.";
        assertTrue(expectedMessage.contains(exception.getMessage()));
    }

    @Test
    void deleteUser() {
        userService.saveUser(user1);
        userService.getUsers().remove(1L);
        assertNull(userService.getUsers().get(1L));
    }


    @Test
    void updateFirstAndLastName() {
        userService.saveUser(user1);
        UserUpdateRequest updateRequest = new UserUpdateRequest("Taras", "Kuhar");
        User updatedUser = userService.updateFirstAndLastName(1L, updateRequest);
        assertEquals("Taras", updatedUser.getFirstName());
        assertEquals("Kuhar", updatedUser.getLastName());
    }

    @Test
    void updateUser() {
        userService.saveUser(user1);
        userService.updateUser(1L, user2);
        assertEquals(user2, userService.getUsers().get(1L));
    }

    @Test
    void testSearchUserByBadRange() {
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            userService.searchUsersByBirthDateRange(LocalDate.now(), LocalDate.now());
        });
        String expectedMessage = "From date must be less than To date.";
        assertTrue(expectedMessage.contains(exception.getMessage()));
    }

    @Test
    void testSearchUserByRange() {
        userService.saveUser(user1);
        userService.saveUser(user2);
        List<User> users = userService.searchUsersByBirthDateRange(LocalDate.of(2000, 8, 25), LocalDate.of(2015, 8, 25));
        assertTrue(users.contains(user2));
    }

    @Test
    void testSearchUserByRangeBadCondition() {
        userService.saveUser(user1);
        userService.saveUser(user2);
        List<User> users = userService.searchUsersByBirthDateRange(LocalDate.of(1980, 8, 25), LocalDate.of(1985, 8, 25));
        assertTrue(users.isEmpty());
    }

    @Test
    void updateFirstAndLastNameWithNotFoundId() {
        UserUpdateRequest updateRequest = new UserUpdateRequest("Taras", "Kuhar");
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updateFirstAndLastName(45L, updateRequest);
        });
        assertTrue(exception.getMessage().contains("Such user does not found."));
    }

    @Test
    void deleteUserWithNoFoundId() {
        Exception exception = assertThrows(UserNotFoundException.class,
                () -> {
                    userService.deleteUser(45L);
                });
        assertTrue(exception.getMessage().contains("Such user does not found."));
    }

    @Test
    void updateUserWithNotFoundId() {
        Exception exception = assertThrows(UserNotFoundException.class,
                () -> {
                    userService.updateUser(45L, this.user2);
                });
        assertTrue(exception.getMessage().contains("Such user does not found."));
    }
}