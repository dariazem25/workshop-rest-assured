package specs;

import configuration.ConfProperties;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

import static io.restassured.RestAssured.given;

public class Specs {

    private static final String BASE_URI = ConfProperties.getProperty("baseUri");

    @Getter
    private final RequestSpecification requestSpecification = given()
            .baseUri(BASE_URI)
            .contentType(ContentType.JSON);
}
