package com.itransition.task4.controller.rest;

import com.itransition.task4.model.dto.UserDTO;
import com.itransition.task4.service.UserService;
import jdk.swing.interop.SwingInterOpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/user", produces = "application/json")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getUsersList() {
        return userService.getAllUserDTOs();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> processAction(@RequestParam String action, @RequestBody List<Long> selectedUserIds) {
        try {
            userService.processAction(action, selectedUserIds);
            return new ResponseEntity<>("Processed admin action.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Processing action has failed: access denied.", HttpStatus.FORBIDDEN);
        }
    }
}
