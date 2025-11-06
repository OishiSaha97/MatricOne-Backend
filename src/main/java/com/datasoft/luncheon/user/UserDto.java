package com.datasoft.luncheon.user;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String username;
    private String employeeId;
    private String password;
    private String fullName;
    private String employeeType;
    private String team;
}
