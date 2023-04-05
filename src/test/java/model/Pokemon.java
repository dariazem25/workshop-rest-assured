package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashMap;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pokemon {

    private String name;
    private Integer weight;

    public List<LinkedHashMap<String, Object>> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<LinkedHashMap<String, Object>> abilities) {
        this.abilities = abilities;
    }

    private List<LinkedHashMap<String, Object>> abilities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
