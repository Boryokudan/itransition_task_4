package com.itransition.task4.service;

import com.itransition.task4.model.User;
import com.itransition.task4.model.UserRegistrationForm;
import com.itransition.task4.model.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.naming.OperationNotSupportedException;
import java.util.List;

public interface UserService extends UserDetailsService {

    User getUserByEmail(String email);

    List<User> getUsers();

    User registerUser(UserRegistrationForm registrationForm);

    void processAction(String action, List<Long> userIds) throws OperationNotSupportedException;

    void blockAllUsersById(List<Long> ids);

    void unblockAllUsersById(List<Long> ids);

    User save(User user);

    void deleteAllById(List<Long> ids);

    List<UserDTO> getAllUserDTOs();

}
