package com.binarxbca.chapter6.service.impl;

import com.binarxbca.chapter6.model.User;
import com.binarxbca.chapter6.model.UserDetailsImpl;
import com.binarxbca.chapter6.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseGet(null);
        return UserDetailsImpl.build(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User user){
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent()){
            final User result = userOptional.get();
            result.setEmail(user.getEmail());
            result.setPassword(user.getPassword());
            result.setUsername(user.getUsername());
            return userRepository.save(result);
        } else {
            return null;
        }
    }
}
