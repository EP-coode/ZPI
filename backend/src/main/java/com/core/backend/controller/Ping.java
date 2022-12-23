package com.core.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "ping")
public class Ping {

    @GetMapping()
    public ResponseEntity<Object> ping() {
        return new ResponseEntity<Object>("pong", HttpStatus.OK);
    }
}
