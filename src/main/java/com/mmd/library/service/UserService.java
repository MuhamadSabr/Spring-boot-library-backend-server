package com.mmd.library.service;

import com.mmd.library.Repository.UserConfirmationRepository;
import com.mmd.library.Repository.authentication.RoleRepository;
import com.mmd.library.Repository.authentication.UserRepository;
import com.mmd.library.constant.RoleConstants;
import com.mmd.library.dto.RegisterNewUser;
import com.mmd.library.entity.authentication.Role;
import com.mmd.library.entity.authentication.User;
import com.mmd.library.entity.authentication.UserConfirmation;
import com.mmd.library.exception.UserConfirmationException;
import com.mmd.library.exception.UserDoesNotExistException;
import com.mmd.library.exception.UserExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Service
public class UserService {

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    private UserConfirmationRepository userConfirmationRepository;

    private EmailService emailService;

    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserConfirmationRepository userConfirmationRepository, RoleRepository roleRepository,
                       EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userConfirmationRepository = userConfirmationRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createNewUser(RegisterNewUser registerNewUser) throws UserExistsException{
        User newUser = new User(registerNewUser.getUsername(), 0, LocalDate.now(), registerNewUser.getPassword(),
                Set.of(roleRepository.findById("ROLE_USER").get()));

        if(userRepository.findByUsername(newUser.getUsername())!=null){
            throw new UserExistsException("A user with the same username exists");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User savedUser = userRepository.save(newUser);
        UserConfirmation userConfirmation = new UserConfirmation(savedUser);
        userConfirmationRepository.save(userConfirmation);

        emailService.sendEmailConfirmation(newUser.getUsername(), userConfirmation.getToken());
    }

    @Transactional
    public void verifyEmail(String token) throws UserConfirmationException{
        UserConfirmation userConfirmation = userConfirmationRepository.findByToken(token);
        if(userConfirmation==null){
            throw new UserConfirmationException("Confirmation not found");
        }
        userConfirmation.getUser().setActive(1);
        userRepository.save(userConfirmation.getUser());
        userConfirmationRepository.delete(userConfirmation);
    }

}
