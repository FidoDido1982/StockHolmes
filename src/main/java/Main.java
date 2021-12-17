import FlightPlan.FlightPlan;
import FlightPlan.FlightPlanSetupAction;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import static java.lang.System.exit;
import static utils.FileUtils.readTextFile;
import static utils.FileUtils.readTextFileFromResources;

public class Main {
    final static Logger logger = LoggerFactory.getLogger(Main.class);
    static FlightPlan flightPlan;

    public static void runSQLFile(String fileName) {
        logger.info("Running runSQLFile. File Name: " + fileName);
        Map dbConfig = flightPlan.getConfig().getDBConfig();
        if (dbConfig == null) {
            logger.error("Could not load DB configuration DBConfig!");
            return;
        }
        // Get parameters to connect to the DB
        String url = flightPlan.getConfig().getDBConfig().get("url").toString();
        String username = flightPlan.getConfig().getDBConfig().get("username").toString();
        String password = flightPlan.getConfig().getDBConfig().get("password").toString();

        // SQL command to create a database in MySQL.
        String sql = readTextFileFromResources(fileName);
        if (sql == null) {
            logger.error("Could not read SQL file " + fileName);
            return;
        }

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            logger.info("Successfully created SQL connection to " + url);
        } catch (SQLException e) {
            logger.error("Could not establish SQL connection. URL: " + url + ", username: " + username +
                    ", password: " + password);
            return;
        }
        String[] queries = sql.split(";");
        for (String query : queries) {
            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.execute();
            } catch (SQLException ex) {
                logger.error("Could not execute SQL query " + query);
                return;
            }
        }
        logger.info("Done executing " + queries.length + " queries.");
    }

    private static void invokeMethod(String methodName, Object[] params) {
        Class[] argTypes = Arrays.stream(params).map(Object::getClass).toArray(Class[]::new);
        try {
            Method method = Main.class.getMethod(methodName, argTypes);
            method.invoke(null, params);
        } catch (NoSuchMethodException exception) {
            logger.error("Failed executing a method called " + methodName + " with " + argTypes.length + " parameters.");
        } catch (IllegalAccessException | InvocationTargetException exception) {
            logger.error(exception.toString());
            logger.error(exception.getMessage());
        }
    }

    public static void main(String[] args) {
        DOMConfigurator.configure("log4j.xml");
        logger.info("Starting StockHolmes!");
        if (args.length != 1) {
            logger.error("Syntax: main <FlightPlan.json>");
            exit(0);
        }
        flightPlan = new FlightPlan(args[0]);

        FlightPlanSetupAction[] setupActions = flightPlan.getSetup().getActions();
        for (int i = 0; i < setupActions.length; i++) {
            if (setupActions[i].getEnabled())
                invokeMethod(setupActions[i].getType(), setupActions[i].getParams());
        }
    }
}
