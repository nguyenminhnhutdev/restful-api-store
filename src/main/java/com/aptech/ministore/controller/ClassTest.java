package com.aptech.ministore.controller;

import com.aptech.ministore.dto.LoginRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassTest {
    @GetMapping(value = "/testUrl")
    public String testUrl(){
        return "Connect success";
    }

    @GetMapping(value = "/auth")
    public String test(){
        return "Connect success";
    }

    @PostMapping(value = "/login")
    public String login(@RequestBody LoginRequest loginRequest){
        return "okok";
    }
}
