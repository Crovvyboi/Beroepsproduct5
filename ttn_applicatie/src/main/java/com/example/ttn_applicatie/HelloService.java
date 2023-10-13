package com.example.ttn_applicatie;

import org.bson.BsonDateTime;
import org.json.*;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class HelloService {

    @RequestMapping(
            value="/",
            method= RequestMethod.GET)
    public String hello() throws JSONException {
        BsonArray array = TtnApplicatieApplication.DBConn();

        if (array != null){
            String arrayString = "";

            // begin construction on table
            arrayString += "<table>";
            arrayString += "<tr><td> Date/Time </td><td> Temperatuur </td><td> pH-waarde </td></tr>";

            // Make a json object from all bsonvalues
            for (BsonValue value:
                 array.getValues()) {
                BsonDocument doc = new BsonDocument();
                doc.put("Value", value);
                // JsonWriterSettings.Builder settingsbuilder = JsonWriterSettings.builder().indent(true).outputMode(JsonMode.EXTENDED);
                JSONObject jsonObject = new JSONObject(doc.toJson());

                String temp = String.valueOf(jsonObject.getJSONObject("Value").getJSONObject("sensor_readings").getDouble("Temperatuur"));
                String pH = String.valueOf(jsonObject.getJSONObject("Value").getJSONObject("sensor_readings").getDouble("pH"));
                String dateString = jsonObject.getJSONObject("Value").getJSONObject("received_at").getString("$date");
                // Date receivedAt = new Date(dateString.toString());
                
                arrayString += "<tr><td> " + dateString + " </td><td> " + temp + "</td><td> " + pH + " </td></tr>";
            }

            arrayString += "</table>";

            return arrayString;
        }

        return "hello";
    }
}
