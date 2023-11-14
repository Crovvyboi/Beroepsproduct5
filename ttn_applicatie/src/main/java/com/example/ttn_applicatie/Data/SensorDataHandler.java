package com.example.ttn_applicatie.Data;

import com.example.ttn_applicatie.Data.DAO.SensorDataDAO;
import com.example.ttn_applicatie.IData.ISensorDataHandler;
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
import org.bson.json.JsonWriterSettings;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SensorDataHandler implements ISensorDataHandler {
    private String connectionString = "mongodb+srv://202324_AD:VdSPg3g77xFGSTmb@cluster0.hpcak9m.mongodb.net/?retryWrites=true&w=majority";

    public ArrayList<SensorDataDAO> GetAll(){
        

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

                ArrayList<SensorDataDAO> daoList = new ArrayList<>();
                daoList = GetSensorDAOList(bsonarray);

                return daoList;
            } catch (MongoException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    public BsonArray GetByBatch(int pageNumber, int batchSize){
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
                collection.find().skip(batchSize * pageNumber).limit(batchSize).forEach(doc -> bsonarray.add(doc.toBsonDocument()));

                return bsonarray;
            } catch (MongoException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public ArrayList<SensorDataDAO> GetSensorDAOList(BsonArray bson) throws JSONException {
        ArrayList<SensorDataDAO> list = new ArrayList<>();

        for (BsonValue value:
                bson.getValues()) {
            BsonDocument doc = new BsonDocument();
            doc.put("Value", value);
            JSONObject jsonObject = new JSONObject(doc.toJson());

            String id = String.valueOf(jsonObject.getJSONObject("Value").getJSONObject("_id").getString("$oid"));
            String deviceID = String.valueOf(jsonObject.getJSONObject("Value").getString("device_id"));
            Double temp = Double.valueOf(String.valueOf(jsonObject.getJSONObject("Value").getJSONObject("sensor_readings").getDouble("Temperatuur")));
            Double pH = Double.valueOf(String.valueOf(jsonObject.getJSONObject("Value").getJSONObject("sensor_readings").getDouble("pH")));
            String dateString = jsonObject.getJSONObject("Value").getJSONObject("received_at").getString("$date");

            ZonedDateTime dateTimeZoned = ZonedDateTime.parse(dateString);
            LocalDateTime dateTime = dateTimeZoned.toLocalDateTime();

            SensorDataDAO dao = new SensorDataDAO(
                    id,
                    dateTime,
                    deviceID,
                    temp,
                    pH
            );

           list.add(dao);

           System.out.println();
        }

        if (list.size() > 0){
            return list;
        }
        return null;
    }
}
