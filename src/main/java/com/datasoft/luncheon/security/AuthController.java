package com.datasoft.luncheon.security;
import com.datasoft.luncheon.user.UserDto;
import com.datasoft.luncheon.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestParam String username, @RequestParam String password) throws UserPrincipalNotFoundException {
        return  new ResponseEntity<>(
                userService.authenticate(username, password), HttpStatus.OK);
    }

    @GetMapping("/i-check")
    public ResponseEntity<?> checkPermission()  {
        return userService.checkPermission();
    }
    @GetMapping("/userIds")
    public ResponseEntity<?> userIds()  {
        return userService.userIds();
    }

    @PostMapping("/user-list")
    public ResponseEntity<?> userList(@RequestParam Long page,
                                      @RequestParam(required = false) String team,
                                      @RequestParam(required = false) String employeeType,
                                      @RequestParam(required = false) String searchParam
     )  {
        return userService.userList(page, team, employeeType, searchParam);
    }
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserDto payload)  {
        return userService.createUser(payload);
    }

}