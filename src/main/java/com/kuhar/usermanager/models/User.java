package com.kuhar.usermanager.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.validator.EmailValidator;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String address;
    private String phoneNumber;

    public boolean isBirthdayValid() {
        return this.birthday.isBefore(LocalDate.now());
    }

    public boolean isEmailValid() {
        return EmailValidator.getInstance()
                .isValid(this.email);
    }

    public boolean areFieldsNull() {
        return email == null
                || firstName == null
                || lastName == null
                || birthday == null
                || address == null
                || phoneNumber == null;
    }
}
