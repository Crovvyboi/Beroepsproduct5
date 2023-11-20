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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SensorDataHandler {
    private String connectionString = "mongodb+srv://202324_AD:VdSPg3g77xFGSTmb@cluster0.hpcak9m.mongodb.net/?retryWrites=true&w=majority";

    @RequestMapping(
            value="/",
            method= RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<String> GetAll(){
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

                JsonWriterSettings jsonSettings = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();

                BsonArray bsonarray = new BsonArray();
                collection.find().forEach(doc -> bsonarray.add(doc.toBsonDocument()));

                JSONArray json = JSONArrayBuilder(bsonarray);

                if (json.length() < 1){
                    return ResponseEntity.notFound().build();
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
            BsonDocument doc = new BsonDocument();
            doc.put("Value", value);

            JSONObject obj = new JSONObject(value.asDocument().toJson());
            jsonObject.put(obj);
        }

        return jsonObject;
    }
}
