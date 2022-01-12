package FlightPlan;
import org.json.JSONObject;

import static utils.FileUtils.getJSONObjectFromName;

public class FlightPlan {
    private static FlightPlan flightPlan;
    private FlightPlanConfig config;
    private FlightPlanSetup setup;

    private FlightPlan(JSONObject JSONObject) {
        config = new FlightPlanConfig(JSONObject.getJSONObject("config"));
        setup = new FlightPlanSetup(JSONObject.getJSONArray("setup"));
    }

    public static void setFlightPlan(String jsonFileName) {
        flightPlan = new FlightPlan(getJSONObjectFromName(jsonFileName));
    }

    public static FlightPlan getFlightPlan() {
        return flightPlan;
    }

    public FlightPlanConfig getConfig() {
        return config;
    }

    public FlightPlanSetup getSetup() {
        return setup;
    }
}
