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
import java.util.Arrays;

import static java.lang.System.exit;

public class Main {
    final static Logger logger = LoggerFactory.getLogger(Main.class);
    static FlightPlan flightPlan;

    public static void runSQLFile(String fileName) {
        logger.info("Running runSQLFile. File Name: " + fileName);
        // Defines the JDBC URL. As you can see, we are not specifying
        // the database name in the URL.
        String url = flightPlan.getConfig().getDBConfig().get("url").toString();

        // Defines username and password to connect to database server.

        String username = flightPlan.getConfig().getDBConfig().get("username").toString();
        String password = flightPlan.getConfig().getDBConfig().get("password").toString();

        // SQL command to create a database in MySQL.
        String sql = "CREATE DATABASE IF NOT EXISTS DEMODB";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            logger.info("After creating a connection");
            stmt.execute();
            logger.info("Done");
        } catch (Exception e) {
            logger.error("Could not connect to database or execute SQL command.");
            e.printStackTrace();
        }
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
            invokeMethod(setupActions[i].getType(), setupActions[i].getParams());
        }
    }
}
