package server;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String action;
    private Map<String, String> data;

    public Request() {
        this.data = new HashMap<>();
    }

    public Request(String action, Map<String, String> data) {
        this.action = action;
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getValue(String key) {
        if (data == null) {
            return null;
        }
        return data.get(key);
    }
}