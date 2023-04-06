import com.fasterxml.jackson.core.JsonProcessingException;
import model.Ability;
import model.Pokemons;
import model.Pokemon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ApiTests extends BaseTest {

    @Test
    @DisplayName("Get valid pokemon")
    public void getValidPokemonTest() throws JsonProcessingException {
        String response = requestSpecification
                .pathParam("name", "rattata")
                .when()
                    .get(POKEMON_ENDPOINT + "/{name}")
                .then()
                    .spec(responseSpecification)
                .extract()
                    .body().asString();

        Pokemon pokemon = TestUtils.toPokemon(response);

        Assertions.assertFalse(pokemon.getAbilities().isEmpty(), "The pokemon doesn't have abilities");
        Assertions.assertEquals("rattata", pokemon.getName(), "The names different");
        Assertions.assertEquals(35, pokemon.getWeight(), "The weights different");
    }

    @Test
    @DisplayName("Get non-existent pokemon")
    public void getNonexistentPokemonTest() {
        String actualResult = requestSpecification
                .pathParam("name", "aaaaZZZ")
                .when()
                    .get(POKEMON_ENDPOINT + "/{name}")
                .then()
                    .assertThat()
                    .statusCode(404)
                .extract()
                    .body().asString();

        Assertions.assertEquals("Not Found", actualResult);
    }

    @Test
    @DisplayName("Compare two existent pokemons")
    public void compareTwoPokemonsTest() throws JsonProcessingException {
        String response1 = requestSpecification
                .pathParam("name", "rattata")
                .when()
                    .get(POKEMON_ENDPOINT + "/{name}")
                .then()
                    .spec(responseSpecification)
                .extract()
                    .body().asString();

        String response2 = requestSpecification
                .pathParam("name", "pidgeotto")
                .when()
                    .get(POKEMON_ENDPOINT + "/{name}")
                .then()
                    .spec(responseSpecification)
                .extract()
                    .body().asString();

        Pokemon rattata = TestUtils.toPokemon(response1);
        Pokemon pidgeotto = TestUtils.toPokemon(response2);

        List<Ability> abilityRattata = rattata.getAbilities().stream()
                .filter(a -> a.getName().equals("run-away"))
                .collect(Collectors.toList());

        List<Ability> abilityPidgeotto = pidgeotto.getAbilities().stream()
                .filter(a -> a.getName().equals("run-away"))
                .collect(Collectors.toList());

        Assertions.assertTrue(rattata.getWeight() < pidgeotto.getWeight(), pidgeotto.getName() + "weight more than " + rattata.getWeight());
        Assertions.assertFalse(abilityRattata.isEmpty(), "Pokemon doesn't have such ability");
        Assertions.assertTrue(abilityPidgeotto.isEmpty(), "Pokemon has such ability");
    }

    @Test
    @DisplayName("List pokemons")
    public void getListOfPokemonsTest() throws JsonProcessingException {
        String response = requestSpecification
                .queryParam("limit", 5)
                .when()
                    .get(POKEMON_ENDPOINT)
                .then()
                    .spec(responseSpecification)
                .extract()
                    .body().asString();

        Pokemons pokemons = TestUtils.toPokemons(response);

        var names = pokemons.getResults().stream().filter(p -> p.getName() != null).collect(Collectors.toList());
        Assertions.assertEquals(POKEMONS_COUNT, pokemons.getCount(), "The counts are different");
        Assertions.assertEquals(5, pokemons.getResults().size(), "The count of pokemons does not equal to entered limit");
        Assertions.assertEquals(5, names.size(), "Not each pokemon has name");
    }

    @Test
    @DisplayName("List pokemons without limit")
    public void getListOfPokemonsWithoutLimitTest() throws JsonProcessingException {
        String response = requestSpecification
                .when()
                    .get(POKEMON_ENDPOINT)
                .then()
                    .spec(responseSpecification)
                .extract()
                    .body().asString();

        Pokemons pokemons = TestUtils.toPokemons(response);

        Assertions.assertEquals(DEFAULT_LIMIT, pokemons.getResults().size(), "The count are not the same");
        Assertions.assertEquals(POKEMONS_COUNT, pokemons.getCount(), "The counts are different");
    }

    @Test
    @DisplayName("List pokemons without limit")
    public void getListOfPokemonsWithZeroLimitTest() throws JsonProcessingException {
        String response = requestSpecification
                .queryParam("limit", 0)
                .when()
                    .get(POKEMON_ENDPOINT)
                .then()
                    .spec(responseSpecification)
                .extract()
                    .body().asString();

        Pokemons pokemons = TestUtils.toPokemons(response);

        Assertions.assertEquals(DEFAULT_LIMIT, pokemons.getResults().size(), "The count are not the same");
        Assertions.assertEquals(POKEMONS_COUNT, pokemons.getCount(), "The counts are different");
    }

    @Test
    @DisplayName("List pokemons with limit less than zero")
    public void getListOfPokemonsWitLimitLessThanZeroTest() throws JsonProcessingException {
        String response = requestSpecification
                .queryParam("limit", -1)
                .when()
                    .get(POKEMON_ENDPOINT)
                .then()
                    .spec(responseSpecification)
                .extract()
                    .body().asString();

        Pokemons pokemons = TestUtils.toPokemons(response);

        Assertions.assertEquals(POKEMONS_COUNT - 1, pokemons.getResults().size(), "The counts are not the same");
        Assertions.assertEquals(POKEMONS_COUNT, pokemons.getCount(), "The counts are different");
    }

    @Test
    @DisplayName("List pokemons with limit greater than the count of pokemons")
    public void getListOfPokemonsWitLimitGreaterThanMaxSizeTest() throws JsonProcessingException {
        String response = requestSpecification
                .queryParam("limit", POKEMONS_COUNT + 1)
                .when()
                    .get(POKEMON_ENDPOINT)
                .then()
                    .spec(responseSpecification)
                .extract()
                    .body().asString();

        Pokemons pokemons = TestUtils.toPokemons(response);

        Assertions.assertEquals(POKEMONS_COUNT, pokemons.getResults().size(), "The counts are not the same");
        Assertions.assertEquals(POKEMONS_COUNT, pokemons.getCount(), "The counts are different");
    }

    @Test
    @DisplayName("List pokemons with invalid limit")
    public void getListWithInvalidLimitTest() throws JsonProcessingException {
        String response = requestSpecification
                .queryParam("limit", "aaa")
                .when()
                    .get(POKEMON_ENDPOINT)
                .then()
                    .spec(responseSpecification)
                .extract()
                    .body().asString();

        Pokemons pokemons = TestUtils.toPokemons(response);

        Assertions.assertEquals(DEFAULT_LIMIT, pokemons.getResults().size(), "The counts are not the same");
        Assertions.assertEquals(POKEMONS_COUNT, pokemons.getCount(), "The counts are different");
    }
}
