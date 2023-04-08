package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Pokemon;
import model.Pokemons;
import specs.Specs;

public class TestUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();
    private final Specs specs = new Specs();

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }

    public Pokemon requestPokemon(String endpoint, String pokemonName) throws JsonProcessingException {
        String response = specs.getRequestSpecification()
                .pathParam("name", pokemonName)
                .when()
                    .get(endpoint + "/{name}")
                .then()
                    .spec(specs.getResponseSpecification())
                .extract()
                    .body().asString();

        return fromJson(response, new TypeReference<>() {});
    }

    public Pokemons requestListOfPokemons(String endpoint, Object limit) throws JsonProcessingException {
        String response = specs.getRequestSpecification()
                .queryParam("limit", limit)
                .when()
                    .get(endpoint)
                .then()
                    .spec(specs.getResponseSpecification())
                .extract()
                    .body().asString();

        return fromJson(response, new TypeReference<>() {});
    }
}
