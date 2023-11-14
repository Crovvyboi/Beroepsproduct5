package com.example.ttn_applicatie;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.BsonArray;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.JsonParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@SpringBootApplication
public class TtnApplicatieApplication {
	public static void main(String[] args) {
		SpringApplication.run(TtnApplicatieApplication.class, args);
	}

}
