package com.erezkatz.StockHolmes.FlightPlan;

import org.json.JSONObject;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FlightPlanSetupAction {
    private String type;
    private Object[] params;

    FlightPlanSetupAction(JSONObject setupActionJSONObject) {
        type = setupActionJSONObject.getString("type");
        params = StreamSupport
                .stream(setupActionJSONObject.getJSONArray("params").spliterator(), false)
                .toArray(Object[]::new);
    }

    public String getType() {
        return type;
    }

    public Object[] getParams() {
        return params;
    }
}
