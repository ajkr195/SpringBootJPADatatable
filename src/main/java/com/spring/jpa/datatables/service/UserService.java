package com.spring.jpa.datatables.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.jpa.datatables.domain.User;
import com.spring.jpa.datatables.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    
    //@Autowired
    //UserRepository userRepository;

    private final UserRepository userRepository;
    
    /* Constructor Injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    } */

    public List<User> findAllUsers() {
        log.debug("Request to get all Users");
        return userRepository.findAll();
    }
    
    public Page<User> findAllTutorials(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

}