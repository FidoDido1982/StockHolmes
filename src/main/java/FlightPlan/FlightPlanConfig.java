package FlightPlan;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import static utils.FileUtils.getJSONObjectFromName;
import static utils.FileUtils.getMapFromShallowJsonObject;

public class FlightPlanConfig {
    private Map DBConfig;

    FlightPlanConfig(JSONObject configJSONObject) {
        DBConfig = getMapFromJsonObject((JSONObject) configJSONObject.get("DBConfig"));
    }

    private Map getMapFromJsonObject(JSONObject jsonObject) {
        Map fieldsMap = getMapFromShallowJsonObject(jsonObject);
        if (fieldsMap != null && fieldsMap.containsKey("FILE")) {
            Map fieldsFromFile = getMapFromShallowJsonObject(getJSONObjectFromName(fieldsMap.get("FILE").toString()));
            fieldsMap.remove("FILE");
            fieldsMap.putAll(fieldsFromFile);
        }
        return fieldsMap;
    }

    public Map getDBConfig() {
        return DBConfig;
    }
}
