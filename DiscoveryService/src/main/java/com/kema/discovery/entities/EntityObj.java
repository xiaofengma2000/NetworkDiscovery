package com.kema.discovery.entities;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Represents a entity discovered from network
 */
public class EntityObj {

    @Id
    String id;

    String type;

    Map<String, String> features = new HashMap<>();

    List<EntityObj> subEntities = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, String> features) {
        this.features = features;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<EntityObj> getSubEntities() {
        return subEntities;
    }

    public void setSubEntities(List<EntityObj> subEntities) {
        this.subEntities = subEntities;
    }
}
