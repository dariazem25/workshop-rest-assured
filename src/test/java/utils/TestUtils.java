package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.Pokemon;
import model.Pokemons;
import specs.Specs;

public class TestUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();
    private final Specs specs = new Specs();

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }

    private Response request(String method, String endpoint, RequestSpecification requestSpecification) {
        return requestSpecification
                .when()
                    .request(method, endpoint);
    }

    private String response(Response response, int statusCode, ContentType contentType) {
        return response
                .then()
                    .statusCode(statusCode)
                    .contentType(contentType)
                .extract()
                    .body().asString();

    }

    public Pokemon requestPokemon(String endpoint, String pokemonName, int statusCode) throws JsonProcessingException {
        RequestSpecification requestSpecification = specs.getRequestSpecification().pathParam("name", pokemonName);
        Response res = request("GET", endpoint, requestSpecification);
        return fromJson(response(res, statusCode, ContentType.JSON), new TypeReference<>() {});
    }

    public Pokemons requestListOfPokemons(String endpoint, Object limit, int statusCode) throws JsonProcessingException {
        RequestSpecification requestSpecification = specs.getRequestSpecification().queryParam("limit", limit);
        Response res = request("GET", endpoint, requestSpecification);
        return fromJson(response(res, statusCode, ContentType.JSON), new TypeReference<>() {});
    }

    public String requestNonExistentPokemon(String endpoint, String pokemonName, int statusCode) {
        RequestSpecification requestSpecification = specs.getRequestSpecification().pathParam("name", pokemonName);
        Response res = request("GET", endpoint, requestSpecification);
        return response(res, statusCode, ContentType.TEXT);
    }
}
