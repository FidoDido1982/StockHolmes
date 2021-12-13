package utils;

import FlightPlan.FlightPlan;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
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

    public static String readTextFileFromResources(String fileName) {
        URL resource = FileUtils.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            logger.error("Could not find file " + fileName + " in resources folder!");
            return null;
        } else {
            try {
                return readFromInputStreamReader(new InputStreamReader(FileUtils.class.getResourceAsStream("../" + fileName)));
            } catch (FileNotFoundException e) {
                logger.error("File " + fileName + " not found!");
                return null;
            } catch (IOException e) {
                logger.error("An IO exception has occurred while trying to read file " + fileName);
                return null;
            }
        }
    }

    public static String readFromInputStreamReader(InputStreamReader inputStreamReader) throws IOException {
        BufferedReader reader;
        StringBuilder sb = new StringBuilder();
        reader = new BufferedReader(inputStreamReader);
        String line = reader.readLine();
        while (line != null) {
            sb.append(line);
            // read next line
            line = reader.readLine();
        }
        reader.close();
        return sb.toString();
    }

    public static String readTextFile(String fileName) {
        BufferedReader reader;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            logger.error("An error occurred while trying to read text file " + fileName);
            return null;
        }
        return sb.toString();
    }
}
