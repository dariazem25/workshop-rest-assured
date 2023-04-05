import io.restassured.http.ContentType;
import model.ListPokemons;
import model.Pokemon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class ApiTests extends BaseTest {

    @Test
    @DisplayName("Get valid pokemon")
    public void getValidPokemonTest() {
        Pokemon pokemon = requestSpecification
                .when()
                .get(POKEMON_ENDPOINT + "/rattata")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(Pokemon.class);

        Assertions.assertFalse(pokemon.getAbilities().isEmpty(), "The pokemon doesn't have abilities");
        Assertions.assertEquals("rattata", pokemon.getName(), "The names different");
        Assertions.assertEquals(35, pokemon.getWeight(), "The weights different");
    }

    @Test
    @DisplayName("Get non-existent pokemon")
    public void getNonexistentPokemonTest() {
        String actualResult = requestSpecification
                .when()
                .get(POKEMON_ENDPOINT + "/aaaaZZZ")
                .then()
                .assertThat()
                .statusCode(404)
                .extract().body().asString();

        Assertions.assertEquals("Not Found", actualResult);
    }

    @Test
    @DisplayName("Compare two existent pokemons")
    public void compareTwoPokemonsTest() {
        Pokemon rattata = requestSpecification
                .when()
                .get(POKEMON_ENDPOINT + "/rattata")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(Pokemon.class);

        Pokemon pidgeotto = requestSpecification
                .when()
                .get(POKEMON_ENDPOINT + "/pidgeotto")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(Pokemon.class);

        var abilityRattata = rattata.getAbilities().stream()
                .map(a -> a.get("ability"))
                .filter(a -> a instanceof LinkedHashMap)
                .filter(a -> ((LinkedHashMap<?, ?>) a).get("name").equals("run-away"))
                .collect(Collectors.toList());

        var abilityPidgeotto = pidgeotto.getAbilities().stream()
                .map(a -> a.get("ability"))
                .filter(a -> a instanceof LinkedHashMap)
                .filter(a -> ((LinkedHashMap<?, ?>) a).get("name").equals("run-away"))
                .collect(Collectors.toList());

        Assertions.assertTrue(rattata.getWeight() < pidgeotto.getWeight(), pidgeotto.getName() + "weight more than " + rattata.getWeight());
        Assertions.assertFalse(abilityRattata.isEmpty(), "Pokemon doesn't have such ability");
        Assertions.assertTrue(abilityPidgeotto.isEmpty(), "Pokemon has such ability");
    }

    @Test
    @DisplayName("List pokemons")
    public void getListOfPokemonsTest() {
        ListPokemons pokemons = requestSpecification
                .queryParam("limit", 5)
                .when()
                .get(POKEMON_ENDPOINT)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(ListPokemons.class);

        var names = pokemons.getResults().stream().filter(p -> p.get("name") != null).collect(Collectors.toList());
        Assertions.assertEquals(POKEMONS_COUNT, pokemons.getCount(), "The counts are different");
        Assertions.assertEquals(5, pokemons.getResults().size(), "The count of pokemons does not equal to entered limit");
        Assertions.assertEquals(5, names.size(), "Not each pokemon has name");
    }

    @ParameterizedTest
    @ValueSource(strings = {POKEMON_ENDPOINT + "?limit=0", POKEMON_ENDPOINT})
    @DisplayName("List pokemons without limit")
    public void getListOfPokemonsWithoutLimitTest(String input) {
        ListPokemons pokemons = requestSpecification
                .when()
                .get(input)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(ListPokemons.class);

        Assertions.assertEquals(DEFAULT_LIMIT, pokemons.getResults().size(), "The count are not the same");
        Assertions.assertEquals(POKEMONS_COUNT, pokemons.getCount(), "The counts are different");
    }

    @Test
    @DisplayName("List pokemons with limit less than zero")
    public void getListOfPokemonsWitLimitLessThanZeroTest() {
        final ListPokemons pokemons = requestSpecification
                .queryParam("limit", -1)
                .when()
                .get(POKEMON_ENDPOINT)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(ListPokemons.class);

        Assertions.assertEquals(POKEMONS_COUNT - 1, pokemons.getResults().size(), "The counts are not the same");
        Assertions.assertEquals(POKEMONS_COUNT, pokemons.getCount(), "The counts are different");
    }

    @Test
    @DisplayName("List pokemons with limit greater than the count of pokemons")
    public void getListOfPokemonsWitLimitGreaterThanMaxSizeTest() {
        final ListPokemons pokemons = requestSpecification
                .queryParam("limit", POKEMONS_COUNT + 1)
                .when()
                .get(POKEMON_ENDPOINT)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(ListPokemons.class);

        Assertions.assertEquals(POKEMONS_COUNT, pokemons.getResults().size(), "The counts are not the same");
        Assertions.assertEquals(POKEMONS_COUNT, pokemons.getCount(), "The counts are different");
    }

    @Test
    @DisplayName("List pokemons with invalid limit")
    public void getListWithInvalidLimitTest() {
        final ListPokemons pokemons = requestSpecification
                .queryParam("limit", "aaa")
                .when()
                .get(POKEMON_ENDPOINT)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(ListPokemons.class);

        Assertions.assertEquals(DEFAULT_LIMIT, pokemons.getResults().size(), "The counts are not the same");
        Assertions.assertEquals(POKEMONS_COUNT, pokemons.getCount(), "The counts are different");
    }
}
