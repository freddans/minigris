package com.freddan.minigris.services;

import com.freddan.minigris.entities.User;
import com.freddan.minigris.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public String createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRoles() == null) {
            user.setRoles("USER");

            User result = userRepository.save(user);

            if (result.getId() > 0) {
                System.out.println("user saved as user automatically");

                return "User was saved";
            } else {

                return "ERROR: User could not be saved";
            }
        } else if (user.getRoles() != null) {

            if (user.getRoles().equals("ADMIN") || user.getRoles().equals("USER")) {

                User result = userRepository.save(user);

                if (result.getId() > 0) {

                    System.out.println("user saved with role given");

                    return "User was saved";
                } else {

                    return "ERROR: User could not be saved";
                }
            } else {

                return "ERROR: Roles has to be set as either ADMIN or USER";
            }
        } else {
            return "ERROR: Unknown error when creating a user";
        }
    }

//    public Object findMyDetails() {
//        return userRepository.findByEmail(findLoggedInUserDetails().getUsername());
//    }

    public User findMyDetails() {
        Optional<User> optionalUser = userRepository.findByEmail(findLoggedInUserDetails().getUsername());

        return optionalUser.get();
    }

    public UserDetails findLoggedInUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {

            return (UserDetails) authentication.getPrincipal();
        } else {

            return null;
        }
    }

    public boolean authenticate(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return passwordEncoder.matches(password, user.getPassword());
        } else {
            return false;

        }
    }

}
