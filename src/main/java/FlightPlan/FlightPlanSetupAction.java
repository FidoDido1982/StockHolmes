package FlightPlan;

import org.json.JSONObject;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FlightPlanSetupAction {
    private String type;
    private Object[] params;
    private boolean enabled;

    FlightPlanSetupAction(JSONObject setupActionJSONObject) {
        type = setupActionJSONObject.getString("type");
        params = StreamSupport
                .stream(setupActionJSONObject.getJSONArray("params").spliterator(), false)
                .toArray(Object[]::new);
        enabled = setupActionJSONObject.optBoolean("enabled", false);
    }

    public String getType() {
        return type;
    }

    public Object[] getParams() {
        return params;
    }

    public boolean getEnabled() {
        return enabled;
    }
}
