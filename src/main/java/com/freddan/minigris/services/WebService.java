package com.freddan.minigris.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebService {

    private UserService userService;

    @Autowired
    public WebService(UserService userService) {
        this.userService = userService;
    }
}
