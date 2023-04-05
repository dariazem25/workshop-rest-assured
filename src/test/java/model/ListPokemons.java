package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashMap;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListPokemons {

    private List<LinkedHashMap<String, String>> results;
    private Integer count;

    public List<LinkedHashMap<String, String>> getResults() {
        return results;
    }

    public void setResults(List<LinkedHashMap<String, String>> results) {
        this.results = results;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
