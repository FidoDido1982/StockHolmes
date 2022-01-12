package utils;

import FlightPlan.FlightPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import static utils.FileUtils.readTextFileFromResources;

public class DBUtils {
    private static Connection connection = null;
    final static Logger logger = LoggerFactory.getLogger(DBUtils.class);

    public static Connection getConnection() {
        FlightPlan flightPlan = FlightPlan.getFlightPlan();
        if (connection == null) {
            Map dbConfig = flightPlan.getConfig().getDBConfig();
            if (dbConfig == null) {
                logger.error("Could not load DB configuration DBConfig!");
                return null;
            }
            // Get parameters to connect to the DB
            String url = flightPlan.getConfig().getDBConfig().get("url").toString();
            String username = flightPlan.getConfig().getDBConfig().get("username").toString();
            String password = flightPlan.getConfig().getDBConfig().get("password").toString();

            try {
                connection = DriverManager.getConnection(url, username, password);
                logger.info("Successfully created SQL connection to " + url);
            } catch (SQLException e) {
                logger.error("Could not establish SQL connection. URL: " + url + ", username: " + username +
                        ", password: " + password);
                return null;
            }
        }
        return connection;
    }
}
