package utils;

import FlightPlan.FlightPlan;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    public static JSONObject getJSONObjectFromName(String jsonFileName) {
        InputStream is = FlightPlan.class.getClassLoader().getResourceAsStream(jsonFileName);
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + jsonFileName);
        }
        return new JSONObject(new JSONTokener(is));
    }

    public static Map getMapFromShallowJsonObject(JSONObject jsonObject) {
        try {
            Map returnMap = new HashMap();
            for (String key : jsonObject.keySet()) {
                returnMap.put(key, jsonObject.getString(key));
            }
            return returnMap;
        } catch (JSONException e) {
            // TODO: log
            return null;
        }
    }
}
