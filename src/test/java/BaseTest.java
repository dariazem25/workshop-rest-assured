import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected static final Integer POKEMONS_COUNT = 1279;
    protected static final Integer DEFAULT_LIMIT = 20;
    protected static final String POKEMON_ENDPOINT = "pokemon";

    protected final RequestSpecification requestSpecification = given()
            .baseUri("https://pokeapi.co/api/v2/")
            .contentType(ContentType.JSON);

    @BeforeAll
    protected static void setup() {
        RestAssured.filters(new AllureRestAssured());
    }
}
