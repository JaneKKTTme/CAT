package vartable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VarTable {

    public static class VarData {
        public String type;
        public Object value;

        public VarData(String type, Object value) {
            this.type = type;
            this.value = value;
        }

        public String toString() {
            return type + ", " + value;
        }
    }

    private HashMap<String, VarData> hashMap;
    private static VarTable instance;

    public VarTable() {
        hashMap = new HashMap<>();
    }

    public void add(String var, Object value) {
        hashMap.put(var, new VarData("null", value));
    }

    public void add(String var, String type, Object value) {
        hashMap.put(var, new VarData(type, value));
    }

    public boolean contains(String var) {
        return hashMap.containsKey(var);
    }

    public static VarTable getInstance() {
        if (instance == null) {
            instance = new VarTable();
        }

        return instance;
    }

    public Map<String, VarData> getData() {
        return hashMap;
    }

    public void clear() {
        hashMap.clear();
    }

    public void setData(Map<String, VarData> data) {
        hashMap = new HashMap<>(data);
    }

    public String getType(String var) {
        return hashMap.get(var).type;
    }

    public Object getValue(String var) {
        return hashMap.get(var).value;
    }

    public void setType(String var, String type) {
        hashMap.get(var).type = type;
    }

    public void setValue(String var, Object value) {
        hashMap.get(var).value = value;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        boolean first = true;

        for (Map.Entry<String, VarData> entry : hashMap.entrySet()) {
            if (!first) {
                builder.append(", ");
            }
            builder.append("[" + entry.getKey() + " : " + entry.getValue() + "]");
            first = false;
        }
        builder.append("}");

        return builder.toString();
    }

    public Set<String> keySet() {
        return hashMap.keySet();
    }

    public void remove(String var) {
        hashMap.remove(var);
    }
}
