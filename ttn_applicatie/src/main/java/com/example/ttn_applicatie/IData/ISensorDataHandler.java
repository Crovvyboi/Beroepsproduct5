package com.example.ttn_applicatie.IData;

import com.example.ttn_applicatie.Data.DAO.SensorDataDAO;
import org.bson.BsonArray;
import org.json.JSONArray;

import java.util.List;

public interface ISensorDataHandler {
    public List<SensorDataDAO> GetAll();
    public BsonArray GetByBatch(int pageNumber, int batchSize);

}
