package utils;

import FlightPlan.FlightPlan;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    final static Logger logger = LoggerFactory.getLogger(FileUtils.class);

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
            logger.error("Could not parse JSon object " + jsonObject);
            return null;
        }
    }
}
