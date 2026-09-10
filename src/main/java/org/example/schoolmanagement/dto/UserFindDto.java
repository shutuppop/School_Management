package org.example.schoolmanagement.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFindDto {
    String firstName;
    String secondName;
    String middleName;
    String email;
    String phoneNumber;

    @Override
    public String toString() {
        return firstName + " " + secondName + " " + middleName + " " + email;
    }
}
