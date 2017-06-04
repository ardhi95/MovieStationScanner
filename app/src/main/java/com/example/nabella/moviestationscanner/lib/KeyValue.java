package com.example.nabella.moviestationscanner.lib;

import java.util.ArrayList;

public class KeyValue {

    String key;
    Object value;

    public KeyValue(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return this.key + "=" + this.value.toString();
    }

    public static String makeURIFormat(ArrayList<KeyValue> keyValues) {
        String uri = "";
        if(keyValues.size() < 1)
            return uri;
        for(KeyValue kv : keyValues) {
            uri += (kv.toString() + "&");
        }
        uri = uri.substring(0, (uri.length() - 1));
        return uri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
