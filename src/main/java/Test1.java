import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import static io.restassured.RestAssured.*;

public class Test1 {

    private Gson gson;
    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
        gson = new Gson();
    }


    public static String requestBody = "{\n" +
            "\"email\": \"sydney@fife\"\n" +
            "}";


    @Test
    public void postRequest() {
        Response response = given().contentType("application/json").and().body(requestBody).when().post("/register").then().extract().response();
        System.out.println(response.body().asString());
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertEquals("Missing password", response.jsonPath().getString("error"));
    }


    public static String getBody = "{\n" +
            "    \"data\": {\n" +
            "        \"id\": 2,\n" +
            "        \"email\": \"janet.weaver@reqres.in\",\n" +
            "        \"first_name\": \"Janet\",\n" +
            "        \"last_name\": \"Weaver\",\n" +
            "        \"avatar\": \"https://reqres.in/img/faces/2-image.jpg\"\n" +
            "    },\n" +
            "    \"support\": {\n" +
            "        \"url\": \"https://reqres.in/#support-heading\",\n" +
            "        \"text\": \"To keep ReqRes free, contributions towards server costs are appreciated!\"\n" +
            "    }\n" +
            "}";

    @Test
    public void getList() {

        Response response = given().contentType("application/json").and().body(getBody).when().get("/users/2").then().extract().response();
        System.out.println(response.body().asPrettyString());
        Assertions.assertEquals(200, response.statusCode());

    }


    public static String putRequestBody = "{\n" +
            "\"name\": \"Tr\",\n" +
            "\"job\": \"qa\"\n" +
            "}";
    public static String putRequestBodyUp = "{\n" +
            "\"name\": \"Trayana\",\n" +
            "\"job\": \"dev\"\n" +
            "}";

    public Response responsePost(){
        return      given()
                .header("Content-type", "application/json")
                .and()
                .body(putRequestBody)
                .when()
                .post("/users").then().extract().response();
    }

    @Test
    public void putRequest() {
        Response post = responsePost();

        String id = post.jsonPath().getString("id");
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(putRequestBody)
                .when()
                .put("/users/" + id)
                .then()
                .extract().response();
        String responseBody = response.body().asString();

        System.out.println(putRequestBodyUp);
    }

    @Test
    public void getListResource() {
        Response response = given()
                .when()
                .get("/unknown")
                .then()
                .extract().response();
        Assertions.assertEquals(200, response.statusCode());

        ListResourceDTO dto = gson.fromJson(response.body().asString(), ListResourceDTO.class);
        int expectedPage = 1;
        int expectedPerPage = 6;
        int totals = 12;
        int totalPages = 2;

        Assertions.assertEquals(expectedPage, dto.page);
        Assertions.assertEquals(expectedPerPage, dto.per_page);
        Assertions.assertEquals(totals, dto.total);
        Assertions.assertEquals(totalPages, dto.total_pages);

        for (var element : dto.data) {
            System.out.println(element.name);

        }
    }
}

