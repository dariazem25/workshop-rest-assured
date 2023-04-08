package specs;

import configuration.ConfProperties;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.Getter;

import static io.restassured.RestAssured.given;

public class Specs {

    private static final String BASE_URI = ConfProperties.getProperty("baseUri");

    @Getter
    private final RequestSpecification requestSpecification = given()
            .baseUri(BASE_URI)
            .contentType(ContentType.JSON);

    @Getter
    private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectContentType(ContentType.JSON)
            .build();
}
