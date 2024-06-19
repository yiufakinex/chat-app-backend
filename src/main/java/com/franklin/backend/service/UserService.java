package com.franklin.backend.service;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.franklin.backend.entity.User;
import com.franklin.backend.entity.User.Role;
import com.franklin.backend.exception.InvalidInputException;
import com.franklin.backend.exception.InvalidUsernameException;
import com.franklin.backend.repository.UserRepository;
import com.franklin.backend.util.CustomValidator;

import jakarta.validation.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private final CustomValidator<User> customValidator = new CustomValidator<>();

    public void newUser(User user, String username, OAuth2User oAuth2User) {
        if (userRepository.existsByUsername(username)) {
            throw new InvalidUsernameException(username + " is already taken. Try a different username.", username);
        }
        user.setUsername(username);
        user.setRole(Role.USER);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new InvalidUsernameException(new ConstraintViolationException(violations).getMessage(), username);
        }
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        Optional<User> optional = userRepository.findByUsername(username);
        if (optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    public User findUserJoinedWithGroupChat(String username) {
        Optional<User> optional = userRepository.findUserJoinedWithGroupChat(username);
        if (optional.isEmpty()) {
            throw new InvalidInputException(username + " is invalid.");
        }
        return optional.get();
    }

    public User updateDisplayName(User user, String displayName) {
        user.setDisplayName(displayName);
        customValidator.validate(user);
        return userRepository.save(user);
    }

    public User getUserFromPrincipal(Principal principal) {
        return (User) principal;
    }
}