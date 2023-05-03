package utils;

import com.google.gson.Gson;
import dto.request.UserRequest;
import dto.response.UserTokenResponse;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.json.JSONObject;
import configuration.ConfigurationReader;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Properties;

public class RestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);
    private static final Properties PROPERTIES = ConfigurationReader.getPropertiesFromFile("base.properties");
    private static final int RESPONSE_OK = 200;
    private static final String BASE_URL = PROPERTIES.getProperty("base.url");
    private String authorizationValue;
    private static final String AUTHORIZATION = "Authorization";
    private RequestSpecification client;

    private RestClient() {
        RestAssured.defaultParser = Parser.JSON;
        updateHeaders();
    }

    private void updateHeaders() {
        try {
            authorizationValue = getAuthorizationToken();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    private String getAuthorizationToken() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", PROPERTIES.getProperty("login"));
        jsonObject.put("password", PROPERTIES.getProperty("password"));

        return "Bearer " + RestAssured.given().body(jsonObject.toMap())
                .when().contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .post("Account/V1/GenerateToken")
                .then().statusCode(RESPONSE_OK)
                .log().all().extract().response().getBody()
                .as(UserTokenResponse.class).getToken();
    }

    private static final class RestClientWrapper {
        static RestClient instance = new RestClient();

        private RestClientWrapper() {
        }
    }

    public static RestClient getInstance() {
        return RestClientWrapper.instance;
    }

    private Headers getHeaders() {
        return new Headers(new Header(AUTHORIZATION, authorizationValue));
    }

    public RequestSpecification createRequestSpecification() {
        client = RestAssured.given().headers(getHeaders());
        return client;
    }

    public <T> Response sendRequest(HttpMethod httpMethod, String template, T entity) {
        LOGGER.info("{} {} \n Body: {}", httpMethod, template, new Gson().toJson(entity));
        Response response;
        createRequestSpecification();
        String templatePath = BASE_URL + template;
        response = sendRequestForHttpMethod(httpMethod, client.contentType(ContentType.JSON)
                .body(new Gson().toJson(entity)), templatePath);
        return response;
    }

    private Response sendRequestForHttpMethod(HttpMethod httpMethod, RequestSpecification requestSpecification, String url) {

        return switch (httpMethod) {
            case PUT -> requestSpecification.put(url);
            case POST -> requestSpecification.post(url);
            case GET -> requestSpecification.get(url);
            case DELETE -> requestSpecification.delete(url);
        };
    }

    public Response sendRequest(HttpMethod httpMethod, String templateKey) {
        return sendRequest(httpMethod, templateKey, true);
    }

    public Response sendRequest(HttpMethod httpMethod, String templateKey, boolean IsCheckNeededAfterRequest) {

        Response response = sendRequest(httpMethod, templateKey, new HashMap<>());
        if (IsCheckNeededAfterRequest) {
            GenericRestVerify.checkResponseCoedIs(response, RESPONSE_OK);
        }
        return response;
    }

    public static void authorizeUser(){
        RestClient.getInstance().sendRequest(HttpMethod.POST, "Account/v1/Authorized",
                new UserRequest(PROPERTIES.getProperty("login"), PROPERTIES.getProperty("password")));
    }

}
