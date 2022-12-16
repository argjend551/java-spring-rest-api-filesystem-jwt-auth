package com.example.FileApp.services;


import com.example.FileApp.data.User;
import com.example.FileApp.exceptions.UserAlreadyExistsException;
import com.example.FileApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("A user with username '" + username + "' could not be found."));
    }

    public String getUserIdByUserName(String username) throws UsernameNotFoundException{
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("A user with username '" + username + "' could not be found."));
        return user.getUserId();
    }



    public User registerUser(String username, String password)
            throws UserAlreadyExistsException
    {
        var existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            throw new UserAlreadyExistsException("A user with the specified name already exists.");
        }


        var user = new User(username, passwordEncoder.encode(password));
        return userRepository.save(user);

    }

    public Optional<User> findUserbyId(String userId){
        return userRepository.findById(userId);
    }

}
