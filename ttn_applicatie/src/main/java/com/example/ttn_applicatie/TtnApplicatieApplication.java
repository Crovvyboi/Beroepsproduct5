package com.example.ttn_applicatie;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.JsonParser;

import java.util.Objects;
import java.util.Set;

@SpringBootApplication
public class TtnApplicatieApplication {
	public static void main(String[] args) {
		SpringApplication.run(TtnApplicatieApplication.class, args);

		DBConn();
	}

	public static void DBConn(){
		String connectionString = "mongodb+srv://202324_AD:VdSPg3g77xFGSTmb@cluster0.hpcak9m.mongodb.net/?retryWrites=true&w=majority";
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
				Bson filter = Filters.and(Filters.gt("SensorData", 1), Filters.lt("SensorData", 100));
				collection.find().forEach(doc -> System.out.println(doc.toJson()));

			} catch (MongoException e) {
				e.printStackTrace();
			}
		}
	}

}
