package com.example.demo;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonObject;
import org.bson.json.JsonWriterSettings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;


@CrossOrigin(origins ={"http://localhost:5173/"})
@RestController
@RequestMapping("/api")
public class SensorDataHandler {
    private String connectionString = "mongodb+srv://202324_AD:VdSPg3g77xFGSTmb@cluster0.hpcak9m.mongodb.net/?retryWrites=true&w=majority";

    @RequestMapping(
            value="/",
            method= RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<String> GetAll(@RequestParam(required = false) Integer batchsize, @RequestParam(required = false) Integer batchpos){
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("202324_SensorProj");

                MongoCollection<Document> collection = database.getCollection("SensorData");
                System.out.println("Number of collections in db: " + collection.countDocuments());

                BsonArray bsonarray = new BsonArray();
                if (batchsize == null || batchpos == null){
                    collection.find().forEach(doc -> bsonarray.add(doc.toBsonDocument()));
                }
                else{
                    collection.find().skip(batchpos * batchsize).limit(batchsize).forEach(doc -> bsonarray.add(doc.toBsonDocument()));
                }

                JSONArray json = JSONArrayBuilder(bsonarray);

                if (json.length() < 1){
                    return ResponseEntity.noContent().build();
                }
                else {
                    JSONObject object = new JSONObject();
                    object.put("Object", json);
                    return ResponseEntity.ok(object.toString());
                }
            } catch (MongoException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    // Get outliers (entries that dont fall within guidelines)
    @RequestMapping(
            value="/outliers/",
            method= RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<String> GetOutliers(@RequestParam(required = false) Double o2min, @RequestParam(required = false) Double o2max,
                                              @RequestParam(required = false) Double tempmin, @RequestParam(required = false) Double tempmax,
                                              @RequestParam(required = false) Double phmin, @RequestParam(required = false) Double phmax ){
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("202324_SensorProj");

                MongoCollection<Document> collection = database.getCollection("SensorData");
                System.out.println("Number of collections in db: " + collection.countDocuments());

                BsonArray bsonarray = new BsonArray();
                collection.find().forEach(doc -> bsonarray.add(doc.toBsonDocument()));


                JSONArray json = JSONArrayBuilderOutliers(bsonarray, o2max, o2min, tempmax, tempmin, phmax, phmin);

                if (json.length() < 1){
                    return ResponseEntity.noContent().build();
                }
                else {
                    JSONObject object = new JSONObject();
                    object.put("Object", json);
                    return ResponseEntity.ok(object.toString());
                }
            } catch (MongoException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    // Per date / month
    @RequestMapping(
            value="/bydate/",
            method= RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<String> GetByDate(@RequestParam int day, @RequestParam int month, @RequestParam int year){
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("202324_SensorProj");

                MongoCollection<Document> collection = database.getCollection("SensorData");
                System.out.println("Number of collections in db: " + collection.countDocuments());

                BsonArray bsonarray = new BsonArray();
                collection.find().forEach(doc -> bsonarray.add(doc.toBsonDocument()));

                JSONArray json = JSONArrayBuilder(bsonarray, day, month, year);

                if (json.length() < 1){
                    return ResponseEntity.noContent().build();
                }
                else {
                    JSONObject object = new JSONObject();
                    object.put("Object", json);
                    return ResponseEntity.ok(object.toString());
                }
            } catch (MongoException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    // Check if value is outside wanted perimiter
    @RequestMapping(
            value="/phrange/",
            method= RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<String> GetPHRange(@RequestParam(required = false) Double phmin, @RequestParam(required = false) Double phmax){
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("202324_SensorProj");

                MongoCollection<Document> collection = database.getCollection("SensorData");
                System.out.println("Number of collections in db: " + collection.countDocuments());

                BsonArray bsonarray = new BsonArray();
                collection.find().forEach(doc -> bsonarray.add(doc.toBsonDocument()));

                JSONArray json;

                if (phmax != null && phmin != null){
                    json = JSONArrayBuilderPH(bsonarray, phmax, phmin);
                }
                else if (phmax == null && phmin != null){
                    json = JSONArrayBuilderPH(bsonarray, null, phmin);
                }
                else if (phmax != null && phmin == null){
                    json = JSONArrayBuilderPH(bsonarray, phmax, null);
                }
                else {
                    json = JSONArrayBuilder(bsonarray);
                }

                if (json.length() < 1){
                    return ResponseEntity.noContent().build();
                }
                else {
                    JSONObject object = new JSONObject();
                    object.put("Object", json);
                    return ResponseEntity.ok(object.toString());
                }
            } catch (MongoException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    @RequestMapping(
            value="/temprange/",
            method= RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<String> GetTempRange(@RequestParam(required = false) Double tempmin, @RequestParam(required = false) Double tempmax){
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("202324_SensorProj");

                MongoCollection<Document> collection = database.getCollection("SensorData");
                System.out.println("Number of collections in db: " + collection.countDocuments());

                BsonArray bsonarray = new BsonArray();
                collection.find().forEach(doc -> bsonarray.add(doc.toBsonDocument()));

                JSONArray json;

                if (tempmax != null && tempmin != null){
                    json = JSONArrayBuilderTemp(bsonarray, tempmax, tempmin);
                }
                else if (tempmax == null && tempmin != null){
                    json = JSONArrayBuilderTemp(bsonarray, null, tempmin);
                }
                else if (tempmax != null && tempmin == null){
                    json = JSONArrayBuilderTemp(bsonarray, tempmax, null);
                }
                else {
                    json = JSONArrayBuilder(bsonarray);
                }

                if (json.length() < 1){
                    return ResponseEntity.noContent().build();
                }
                else {
                    JSONObject object = new JSONObject();
                    object.put("Object", json);
                    return ResponseEntity.ok(object.toString());
                }
            } catch (MongoException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    @RequestMapping(
            value="/o2range/",
            method= RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<String> GetO2Range(@RequestParam(required = false) Double o2min, @RequestParam(required = false) Double o2max){
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("202324_SensorProj");

                MongoCollection<Document> collection = database.getCollection("SensorData");
                System.out.println("Number of collections in db: " + collection.countDocuments());

                BsonArray bsonarray = new BsonArray();
                collection.find().forEach(doc -> bsonarray.add(doc.toBsonDocument()));

                JSONArray json;

                if (o2max != null && o2min != null){
                    json = JSONArrayO2Builder(bsonarray, o2max, o2min);
                }
                else if (o2max == null && o2min != null){
                    json = JSONArrayO2Builder(bsonarray, null, o2min);
                }
                else if (o2max != null && o2min == null){
                    json = JSONArrayO2Builder(bsonarray, o2max, null);
                }
                else {
                    json = JSONArrayBuilder(bsonarray);
                }

                if (json.length() < 1){
                    return ResponseEntity.noContent().build();
                }
                else {
                    JSONObject object = new JSONObject();
                    object.put("Object", json);
                    return ResponseEntity.ok(object.toString());
                }
            } catch (MongoException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    public JSONArray JSONArrayBuilder(BsonArray bsonArray) throws JSONException {
        JSONArray jsonObject = new JSONArray();

        for (BsonValue value:
                bsonArray.getValues()) {
            JSONObject obj = new JSONObject(value.asDocument().toJson());
            jsonObject.put(obj);
        }

        return jsonObject;
    }

    public JSONArray JSONArrayBuilder(BsonArray bsonArray, int day, int month, int year) throws JSONException {
        JSONArray jsonObject = new JSONArray();

        for (BsonValue value:
                bsonArray.getValues()) {
            JSONObject obj = new JSONObject(value.asDocument().toJson());

            String dateString = obj.getJSONObject("received_at").getString("$date");
            ZonedDateTime dateTimeZoned = ZonedDateTime.parse(dateString);
            LocalDateTime dateTime = dateTimeZoned.toLocalDateTime();

            if (dateTime.getDayOfMonth() == day && dateTime.getMonthValue() == month && dateTime.getYear() == year){
                jsonObject.put(obj);
            }
        }

        return jsonObject;
    }

    public JSONArray JSONArrayBuilderPH(BsonArray bsonArray, Double phMax, Double phMin) throws JSONException {
        JSONArray jsonObject = new JSONArray();

        for (BsonValue value:
                bsonArray.getValues()) {
            JSONObject obj = new JSONObject(value.asDocument().toJson());

            Double pH = Double.valueOf(String.valueOf(obj.getJSONObject("sensor_readings").getDouble("pH")));


            if (phMax != null && phMin != null){
                if (phMin < pH && pH < phMax){
                    jsonObject.put(obj);
                }
            }
            else if (phMax == null && phMin != null){
                if (phMin < pH){
                    jsonObject.put(obj);
                }
            }
            else if (phMax != null && phMin == null){
                if (pH < phMax){
                    jsonObject.put(obj);
                }
            }
        }

        return jsonObject;
    }

    public JSONArray JSONArrayBuilderTemp(BsonArray bsonArray, Double tempMax, Double tempMin) throws JSONException {
        JSONArray jsonObject = new JSONArray();

        for (BsonValue value:
                bsonArray.getValues()) {
            JSONObject obj = new JSONObject(value.asDocument().toJson());

            Double temp = Double.valueOf(String.valueOf(obj.getJSONObject("sensor_readings").getDouble("Temperatuur")));

            if (tempMax != null && tempMin != null){
                if (tempMin < temp && temp < tempMax){
                    jsonObject.put(obj);
                }
            }
            else if (tempMax == null && tempMin != null){
                if (tempMin < temp){
                    jsonObject.put(obj);
                }
            }
            else if (tempMax != null && tempMin == null){
                if (temp < tempMax){
                    jsonObject.put(obj);
                }
            }
        }

        return jsonObject;
    }

    public JSONArray JSONArrayO2Builder(BsonArray bsonArray, Double o2max, Double o2min) throws JSONException {

        JSONArray jsonObject = new JSONArray();

        for (BsonValue value:
                bsonArray.getValues()) {
            JSONObject obj = new JSONObject(value.asDocument().toJson());

            Double o2 = Double.valueOf(String.valueOf(obj.getJSONObject("sensor_readings").getDouble("O2")));

            if (o2max != null && o2min != null){
                if (o2min < o2 && o2 < o2max){
                    jsonObject.put(obj);
                }
            }
            else if (o2max == null && o2min != null){
                if (o2min < o2){
                    jsonObject.put(obj);
                }
            }
            else if (o2max != null && o2min == null){
                if (o2 < o2max){
                    jsonObject.put(obj);
                }
            }
        }

        return jsonObject;
    }

    public JSONArray JSONArrayBuilderOutliers(BsonArray bsonArray, Double o2max, Double o2min, Double tempMax, Double tempMin, Double phMax, Double phMin) throws JSONException {

        JSONArray jsonObject = new JSONArray();

        for (BsonValue value:
                bsonArray.getValues()) {
            JSONObject obj = new JSONObject(value.asDocument().toJson());

            boolean addEntry = false;

            Double o2 = Double.valueOf(String.valueOf(obj.getJSONObject("sensor_readings").getDouble("O2")));
            Double pH = Double.valueOf(String.valueOf(obj.getJSONObject("sensor_readings").getDouble("pH")));
            Double temp = Double.valueOf(String.valueOf(obj.getJSONObject("sensor_readings").getDouble("Temperatuur")));

            // check temp range
            if (tempMax != null && tempMin != null){
                if (tempMin < temp && temp < tempMax){
                    addEntry = true;
                }
            }
            else if (tempMax == null && tempMin != null){
                if (tempMin < temp){
                    addEntry = true;
                }
            }
            else if (tempMax != null && tempMin == null){
                if (temp < tempMax){
                    addEntry = true;
                }
            }

            // check ph range
            if (phMax != null && phMin != null){
                if (phMin < pH && pH < phMax){
                    addEntry = true;
                }
            }
            else if (phMax == null && phMin != null){
                if (phMin < pH){
                    addEntry = true;
                }
            }
            else if (phMax != null && phMin == null){
                if (pH < phMax){
                    addEntry = true;
                }
            }

            // Check o2 range
            if (o2max != null && o2min != null){
                if (o2min > o2 && o2 > o2max){
                    addEntry = true;
                }
            }
            else if (o2max == null && o2min != null){
                if (o2min > o2){
                    addEntry = true;
                }
            }
            else if (o2max != null && o2min == null){
                if (o2 > o2max){
                    addEntry = true;
                }
            }

            if (addEntry){
                jsonObject.put(obj);
            }
        }

        return jsonObject;
    }
}

