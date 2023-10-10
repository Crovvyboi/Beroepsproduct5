package com.example.ttn_applicatie;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.connection.ClusterDescription;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
public class HelloService {

    @RequestMapping(
            value="/",
            method= RequestMethod.GET)
    public String hello(){


        return "hello";
    }
}
