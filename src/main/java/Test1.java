import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

public class Test1 {

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }


    public static String requestBody = "{\n" +
            "\"email\": \"sydney@fife\"\n" +
            "}";


    @Test
    public void postRequest() {
        Response response = given().contentType("application/json").and().body(requestBody).when().post("/register").then().extract().response();
System.out.println(response.body().asString());
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertEquals("Missing password" , response.jsonPath().getString("error"));
    }

    }


