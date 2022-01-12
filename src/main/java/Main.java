import FlightPlan.FlightPlan;
import FlightPlan.FlightPlanSetupAction;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DBUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import static FlightPlan.FlightPlan.setFlightPlan;
import static java.lang.System.exit;
import static utils.FileUtils.*;

public class Main {
    final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void runSQLFile(String fileName) {
        logger.info("Running runSQLFile. File Name: " + fileName);
        Connection conn = DBUtils.getConnection();
        if (conn == null) {
            logger.error("An error occurred while trying to get DB connection");
            return;
        }
        // SQL command to create a database in MySQL.
        String sql = readTextFileFromResources(fileName);
        if (sql == null) {
            logger.error("Could not read SQL file " + fileName);
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

    public static void exportTable(String tableName, String fileName) {
        logger.info("Starting to export table " + tableName + " to file " + fileName);
        Connection conn = DBUtils.getConnection();
        if (conn == null) {
            logger.error("An error occurred while trying to get DB connection");
            return;
        }
        Map<String, String> configMap = FlightPlan.getFlightPlan().getConfig().getDBConfig();
        if (configMap == null || !configMap.containsKey("secureFilePrivFolder")) {
            logger.error("Could not find secureFilePrivFolder parameter in config file.");
            return;
        }
        if (resourceExists(fileName)) {
            deleteResource(fileName);
            logger.info("Deleted the resource file " + fileName);
        }
        Map<String, String> dbConfig = FlightPlan.getFlightPlan().getConfig().getDBConfig();
        String path = dbConfig.get("secureFilePrivFolder") + fileName;
        String query = "SELECT * INTO OUTFILE \"" + path + "\" FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' " +
                        " LINES TERMINATED BY '\\n' FROM " + dbConfig.get("schema") + "." + tableName + ";";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.execute();
        } catch (SQLException ex) {
            logger.error("Could not execute SQL query " + query);
            logger.error("SQL Exception: " + ex);
            return;
        }
        logger.info("Done exporting!");
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
        setFlightPlan(args[0]);

        FlightPlanSetupAction[] setupActions = FlightPlan.getFlightPlan().getSetup().getActions();
        for (int i = 0; i < setupActions.length; i++) {
            if (setupActions[i].getEnabled())
                invokeMethod(setupActions[i].getType(), setupActions[i].getParams());
        }
    }
}
