package com.mpw.front;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wilk.wojtek@gmail.com.
 */
public class Method {

    @JsonProperty
    private String name;
    @JsonProperty
    private Map<String, String> parameters;

    public Method(){

    }

    public Method(String name,
                  Map<String, String> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    private String getName(){
        return name;
    }

    private Map<String, String> getParameters(){
        return new HashMap<>(parameters);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
