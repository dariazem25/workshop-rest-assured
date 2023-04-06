package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Ability;
import model.Pokemon;
import model.Pokemons;
import model.Result;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static Pokemon toPokemon(final String json) throws JsonProcessingException {
        JsonNode pokemonNode = new ObjectMapper().readTree(json);
        Pokemon pokemon = new Pokemon();
        List<Ability> abilities = new ArrayList<>();

        for (int i = 0; i < pokemonNode.get("abilities").size(); i++) {
            String name = pokemonNode.get("abilities").get(i).get("ability").get("name").textValue();
            Ability ability = new Ability();
            ability.setName(name);
            abilities.add(ability);
        }

        pokemon.setName(pokemonNode.get("name").textValue());
        pokemon.setWeight(pokemonNode.get("weight").asInt());
        pokemon.setAbilities(abilities);
        return pokemon;
    }

    public static Pokemons toPokemons(final String json) throws JsonProcessingException {
        JsonNode pokemonsNode = new ObjectMapper().readTree(json);
        Pokemons pokemons = new Pokemons();
        List<Result> results = new ArrayList<>();

        for (int i = 0; i < pokemonsNode.get("results").size(); i++) {
            String name = pokemonsNode.get("results").get(i).get("name").textValue();
            Result result = new Result();
            result.setName(name);
            results.add(result);
        }

        pokemons.setCount(pokemonsNode.get("count").asInt());
        pokemons.setResults(results);
        return pokemons;
    }
}
