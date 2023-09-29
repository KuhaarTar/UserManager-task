package com.kuhar.usermanager.services;


import com.kuhar.usermanager.exceptions.InvalidInputException;
import com.kuhar.usermanager.exceptions.UserNotFoundException;
import com.kuhar.usermanager.models.User;
import com.kuhar.usermanager.models.UserUpdateRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
@Getter
@Setter
public class UserService {

    @Value(value = "${user.validAge}")
    private int validAge;
    private static Long userId;
    private final HashMap<Long, User> users = new HashMap<>();

    public UserService() {
        UserService.userId = 0L;
    }

    public User saveUser(User user) {
        validateUser(user);
        userId++;
        users.put(userId, user);
        return user;
    }

    public void deleteUser(Long userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
        } else {
            throw new UserNotFoundException("Such user does not found.");
        }
    }

    public User updateFirstAndLastName(Long userId, UserUpdateRequest updateRequest) {
        if (users.containsKey(userId)) {
            User userToUpdate = users.get(userId);
            userToUpdate.setFirstName(updateRequest.firstName());
            userToUpdate.setLastName(updateRequest.lastName());
            users.put(userId, userToUpdate);
            return userToUpdate;
        } else {
            throw new UserNotFoundException("Such user does not found.");
        }
    }

    public User updateUser(Long userId, User updatedUser) {
        User user = users.get(userId);
        validateUser(updatedUser);
        if (users.containsKey(userId)) {
            users.put(userId, updatedUser);
            return updatedUser;
        } else {
            throw new UserNotFoundException("Such user does not found.");
        }
    }

    public List<User> searchUsersByBirthDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(endDate)) {
            List<User> userList = new LinkedList<>(users.values());
            List<User> searchedUsers = new LinkedList<>();
            for (User currentUser : userList) {
                if (currentUser.getBirthday().isAfter(startDate) &&
                        currentUser.getBirthday().isBefore(endDate)) {
                    searchedUsers.add(currentUser);
                }
            }
            return searchedUsers;
        } else {
            throw new InvalidInputException("From date must be less than To date.");
        }
    }

    private void validateUser(User user) {
        notNullUserFields(user);
        validateBirthday(user);
        validateEmail(user);
        isUserAdult(user);
    }

    private void isUserAdult(User user) {
        Period userAge = Period.between(user.getBirthday(), LocalDate.now());
        if (userAge.getYears() < validAge) {
            throw new InvalidInputException("User must be at least " + validAge + " years old.");
        }
    }

    private void validateBirthday(User user) {
        if (!user.isBirthdayValid()) {
            throw new InvalidInputException("Invalid birthday date.");
        }
    }

    private void validateEmail(User user) {
        if (!user.isEmailValid()) {
            throw new InvalidInputException("Invalid email.");
        }
    }

    private void notNullUserFields(User user) {
        if (user.areFieldsNull()) {
            throw new InvalidInputException("Some of the fields are null.");
        }
    }
}
