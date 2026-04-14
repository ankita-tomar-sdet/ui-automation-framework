package utils;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    // Stores data in a Map where the Key is an Enum (for type safety)
    private Map<String, Object> scenarioContext;

    public ScenarioContext() {
        scenarioContext = new HashMap<>();
    }

    public void setContext(Context key, Object value) {
        scenarioContext.put(key.toString(), value);
    }

    public Object getContext(Context string) {
        return scenarioContext.get(string.toString());
    }

    public Boolean isContains(Context key) {
        return scenarioContext.containsKey(key.toString());
    }

    // Enum to define what types of data we expect to share, below data is dummy data, you can modify it as per your requirement
    public enum Context {
        ORDER_ID,
        USER_EMAIL,
        PRODUCT_NAME,
        EXPECTED_DB_STATUS
    }
}