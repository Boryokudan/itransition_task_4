package com.itransition.task4.service.impl;

import com.itransition.task4.mapper.UserMapper;
import com.itransition.task4.model.User;
import com.itransition.task4.model.UserRegistrationForm;
import com.itransition.task4.model.dto.UserDTO;
import com.itransition.task4.repository.UserRepository;
import com.itransition.task4.service.PermissionService;
import com.itransition.task4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceDefImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User not found.");
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        users.sort((person1, person2) -> {
            if (person1.getStatus().equals("Active") && person2.getStatus().equals("Blocked")) {
                return -1;
            } else if (person1.getStatus().equals("Blocked") && person2.getStatus().equals("Active")) {
                return 1;
            } else {
                return person1.getFullName().compareTo(person2.getFullName());
            }
        });
        return users;
    }


    @Override
    public User registerUser(UserRegistrationForm registrationForm) {
        if (registrationForm.getPassword().trim().equals(registrationForm.getConfirmedPassword().trim())) {
            boolean userExists = getUserByEmail(registrationForm.getEmail()) != null;
            if (!userExists) {
                User newUser = new User();
                newUser.setFullName(String.format("%s %s", registrationForm.getFirstName(), registrationForm.getLastName()));
                newUser.setPosition(registrationForm.getPosition());
                newUser.setEmail(registrationForm.getEmail());
                newUser.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
                newUser.setPermissions(List.of(permissionService.getAdminPermission()));
                newUser.setStatus("Active");
                newUser.setRegistrationDateTime(LocalDateTime.now());
                return userRepository.save(newUser);
            }
        }
        return null;
    }

    @Override
    public void processAction(String action, List<Long> userIds) throws OperationNotSupportedException {
        switch (action) {
            case "block" -> blockAllUsersById(userIds);
            case "unblock" -> unblockAllUsersById(userIds);
            case "delete" -> deleteAllById(userIds);
            default -> throw new OperationNotSupportedException(
                    String.format("Operation \"%s\" is not supported/", action)
            );
        }
    }

    @Override
    public void blockAllUsersById(List<Long> ids) {
       List<User> users = userRepository.findAllById(ids);
        List<User> blockedUsers = new ArrayList<>();
        for (User user : users) {
            user.setStatus("Blocked");
            blockedUsers.add(user);
        }
        userRepository.saveAll(blockedUsers);
    }

    @Override
    public void unblockAllUsersById(List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);
        List<User> unblockedUsers = new ArrayList<>();
        for (User user : users) {
            user.setStatus("Active");
            unblockedUsers.add(user);
        }
        userRepository.saveAll(unblockedUsers);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        userRepository.deleteAllById(ids);
    }

    @Override
    public List<UserDTO> getAllUserDTOs() {
        return userMapper.toDTOList(getUsers());
    }
}
