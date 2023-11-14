package com.example.ttn_applicatie.Presentation;

import com.example.ttn_applicatie.Data.DAO.SensorDataDAO;
import com.example.ttn_applicatie.Data.SensorDataHandler;
import com.example.ttn_applicatie.IData.ISensorDataHandler;
import org.json.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
public class DataFrontend {

    @RequestMapping(
            value="/GetAll/",
            method= RequestMethod.GET)
    public String hello() throws JSONException {
        ISensorDataHandler sensorDataHandler = new SensorDataHandler();
        List<SensorDataDAO> array = sensorDataHandler.GetAll();

        if (array != null){
            String arrayString = "";

            // begin construction on table
            arrayString += "<table>";
            arrayString += "<tr><td> Entry # </td><td> Entry ID </td><td> Date/Time </td><td> Temperatuur </td><td> pH-waarde </td></tr>";

            int counter = 0;
            // Make a json object from all bsonvalues
            for (SensorDataDAO value:
                 array) {
                counter++;
                
                arrayString += "<tr><td> " + counter + " </td><td> " + value.getId() + " </td><td> " + value.getReceivedAt() + " </td><td> " + value.getDeviceId() + " </td><td> " + value.getTemperature() + "</td><td> " + value.getPhValue() + " </td></tr>";
            }

            arrayString += "</table>";

            return arrayString;
        }

        return "hello";
    }
}
