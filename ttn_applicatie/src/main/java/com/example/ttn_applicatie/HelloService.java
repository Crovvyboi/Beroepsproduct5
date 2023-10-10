package com.example.ttn_applicatie;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloService {
    @RequestMapping(
            value="/",
            method= RequestMethod.GET)
    public String hello(){
        return "hello";
    }
}
