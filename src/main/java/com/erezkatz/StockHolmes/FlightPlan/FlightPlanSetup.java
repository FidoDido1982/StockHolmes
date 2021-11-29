package com.erezkatz.StockHolmes.FlightPlan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;

public class FlightPlanSetup {
    private FlightPlanSetupAction[] actions;

    FlightPlanSetup(JSONArray setupJSONArray) {
        actions = new FlightPlanSetupAction[setupJSONArray.length()];
        for (int i = 0; i < setupJSONArray.length(); i++) {
            actions[i] = new FlightPlanSetupAction(setupJSONArray.getJSONObject(i));
        }
    }

    public FlightPlanSetupAction[] getActions() {
        return actions;
    }
}
