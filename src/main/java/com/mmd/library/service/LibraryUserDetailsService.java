package com.mmd.library.service;

import com.mmd.library.Repository.authentication.UserRepository;
import com.mmd.library.exception.EmailNotConfirmedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class LibraryUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Set<GrantedAuthority> authorities = new HashSet<>();

        com.mmd.library.entity.authentication.User user = userRepository.findUserByUsernameWithRole(username);
        if (user == null) {
            throw new UsernameNotFoundException("User details not found for the user : " + username);
        }
        if (user.getRoles() == null) {
            throw new UsernameNotFoundException("No authorities are set for the user : " + username);
        }
        if(user.getActive()==0){
            throw new EmailNotConfirmedException("User has not confirmed their email");
        }

        String userName = user.getUsername().trim();
        String password = user.getPassword().trim();
        user.getRoles().forEach(role-> authorities.add(new SimpleGrantedAuthority(role.getRole().trim())));
        return new User(userName,password,authorities);
    }
}
