import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class ApiTest {

    @Test
    public void testGetRequest() {

        String baseUri = "https://reqres.in";
        String postUser = "/api/users";
        String testBody = "{\n" +
                "\"name\": \"morpheus\",\n" +
                "\"job\":\"leader\"\n" +
                "}";
        String jsonSchema = "{\n" +
                "    \"type\": \"object\",\n" +
                "    \"title\": \"Post user\",\n" +
                "    \"required\": [\n" +
                "        \"name\",\n" +
                "        \"job\",\n" +
                "        \"id\",\n" +
                "        \"createdAt\"\n" +
                "    ],\n" +
                "    \"properties\": {\n" +
                "        \"name\": {\n" +
                "            \"type\": \"string\",\n" +
                "            \"title\": \"User name\"\n" +
                "        },\n" +
                "        \"job\": {\n" +
                "            \"type\": \"string\",\n" +
                "            \"title\": \"Job position\"\n" +
                "        },\n" +
                "        \"id\": {\n" +
                "            \"type\": \"string\",\n" +
                "            \"title\": \"User title\"\n" +
                "        },\n" +
                "        \"createdAt\": {\n" +
                "            \"type\": \"string\",\n" +
                "            \"title\": \"User created time\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"additionalProperties\": false\n" +
                "}";

        System.out.println("============================================================================================");
        System.out.println("                        FIRST GET REQUEST WITH STATUS 200");
        System.out.println("============================================================================================");

        //First GET request with status 200
        when().get(baseUri + postUser + "/2").then().statusCode(200).log().all();

        System.out.println("============================================================================================");
        System.out.println("                                SECOND POST REQUEST");
        System.out.println("============================================================================================");

        //Second POST request
        given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .body(testBody)
                .log().all()
                .post(postUser)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body(matchesJsonSchema(jsonSchema));

        System.out.println("============================================================================================");
        System.out.println("                        THIRD GET REQUEST WITH STATUS 200");
        System.out.println("============================================================================================");

        //Third GET request with status 200
        given()
                .baseUri("https://reqres.in/")
                .pathParam("user_id", 2)
                .log().all()
                .get("api/users/{user_id}")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK);

        System.out.println("============================================================================================");
        System.out.println("                        FORTH GET REQUEST WITHOUT STATUS 200");
        System.out.println("============================================================================================");

        //Fourth GET request without status 200
        when().get(baseUri+postUser+"/67").then().log().all();

    }
}
