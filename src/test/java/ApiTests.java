import com.fasterxml.jackson.core.JsonProcessingException;
import configuration.ConfProperties;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import model.Abilities;
import model.Pokemon;
import model.Pokemons;
import model.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.Specs;
import utils.TestUtils;


import java.util.List;
import java.util.stream.Collectors;

public class ApiTests {

    private static final Integer DEFAULT_LIMIT = 20;
    private static final String POKEMON_ENDPOINT = ConfProperties.getProperty("pokemonEndpoint");
    private static final String POKEMON_RATTATA = "rattata";
    private static final String ABILITY_RUN_AWAY = "run-away";
    private final TestUtils testUtils = new TestUtils();
    private final Specs specs = new Specs();

    @BeforeAll
    public static void setup() {
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    @DisplayName("Get valid pokemon")
    public void getValidPokemonTest() throws JsonProcessingException {
        Pokemon pokemon = testUtils.requestPokemon(POKEMON_ENDPOINT, POKEMON_RATTATA);

        Assertions.assertFalse(pokemon.getAbilities().isEmpty(), "The pokemon doesn't have abilities");
        Assertions.assertEquals(POKEMON_RATTATA, pokemon.getName(), "The names different");
        Assertions.assertEquals(35, pokemon.getWeight(), "The weights different");
    }

    @Test
    @DisplayName("Get non-existent pokemon")
    public void getNonexistentPokemonTest() {
        String response = testUtils.requestNonExistentPokemon(POKEMON_ENDPOINT, "aaaaZZZ");

        Assertions.assertTrue(response.contains("Not Found"));
    }

    @Test
    @DisplayName("Compare two existent pokemons")
    public void compareTwoPokemonsTest() throws JsonProcessingException {
        Pokemon rattata = testUtils.requestPokemon(POKEMON_ENDPOINT, POKEMON_RATTATA);
        Pokemon pidgeotto = testUtils.requestPokemon(POKEMON_ENDPOINT, "pidgeotto");

        List<Abilities> abilityRattata = rattata.getAbilities().stream()
                .filter(a -> a.getAbility().getName().equals(ABILITY_RUN_AWAY))
                .collect(Collectors.toList());

        List<Abilities> abilityPidgeotto = pidgeotto.getAbilities().stream()
                .filter(a -> a.getAbility().getName().equals(ABILITY_RUN_AWAY))
                .collect(Collectors.toList());

        Assertions.assertTrue(rattata.getWeight() < pidgeotto.getWeight(), pidgeotto.getName() + "weight more than " + rattata.getWeight());
        Assertions.assertFalse(abilityRattata.isEmpty(), "Pokemon doesn't have such ability");
        Assertions.assertTrue(abilityPidgeotto.isEmpty(), "Pokemon has such ability");
    }

    @Test
    @DisplayName("List pokemons")
    public void getListOfPokemonsTest() throws JsonProcessingException {
        Pokemons pokemons = testUtils.requestListOfPokemons(POKEMON_ENDPOINT, 5);

        List<Result> names = pokemons.getResults().stream().filter(p -> p.getName() != null).collect(Collectors.toList());
        Assertions.assertEquals(5, pokemons.getResults().size(), "The count of pokemons does not equal to entered limit");
        Assertions.assertEquals(5, names.size(), "Not each pokemon has name");
    }

    @Test
    @DisplayName("List pokemons without limit")
    public void getListOfPokemonsWithoutLimitTest() throws JsonProcessingException {
        Pokemons pokemons = testUtils.requestListOfPokemons(POKEMON_ENDPOINT, null);

        Assertions.assertEquals(DEFAULT_LIMIT, pokemons.getResults().size(), "The count are not the same");
    }

    @Test
    @DisplayName("List pokemons with zero limit")
    public void getListOfPokemonsWithZeroLimitTest() throws JsonProcessingException {
        Pokemons pokemons = testUtils.requestListOfPokemons(POKEMON_ENDPOINT, 0);

        Assertions.assertEquals(DEFAULT_LIMIT, pokemons.getResults().size(), "The count are not the same");
    }

    @Test
    @DisplayName("List pokemons with invalid limit")
    public void getListWithInvalidLimitTest() throws JsonProcessingException {
        Pokemons pokemons = testUtils.requestListOfPokemons(POKEMON_ENDPOINT, "aaa");

        Assertions.assertEquals(DEFAULT_LIMIT, pokemons.getResults().size(), "The counts are not the same");
    }
}
