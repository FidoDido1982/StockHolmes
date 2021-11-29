package com.erezkatz.StockHolmes.FlightPlan;
import org.json.JSONObject;

import static com.erezkatz.StockHolmes.utils.FileUtils.getJSONObjectFromName;

public class FlightPlan {
    private FlightPlanConfig config;
    private FlightPlanSetup setup;

    FlightPlan(JSONObject JSONObject) {
        config = new FlightPlanConfig(JSONObject.getJSONObject("config"));
        setup = new FlightPlanSetup(JSONObject.getJSONArray("setup"));
    }

    public FlightPlan(String jsonFileName) {
        this(getJSONObjectFromName(jsonFileName));
    }

    public FlightPlanConfig getConfig() {
        return config;
    }

    public FlightPlanSetup getSetup() {
        return setup;
    }
}
