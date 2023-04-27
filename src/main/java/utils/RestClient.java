package utils;

import dto.response.UserTokenResponse;
import io.restassured.http.ContentType;
import org.json.JSONObject;
import configuration.ConfigurationReader;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private void updateHeaders(){
        try{authorizationValue = getAuthorizationToken();}
        catch(Exception e){
            LOGGER.error(e.getMessage());
        }
    }
    private String getAuthorizationToken(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", PROPERTIES.getProperty("login"));
        jsonObject.put("password", PROPERTIES.getProperty("password"));

        return  "Bearer " + RestAssured.given().body(jsonObject.toMap())
                .when().contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .post("Account/V1/GenerateToken")
                .then().statusCode(RESPONSE_OK)
                .log().all().extract().response().getBody()
                .as(UserTokenResponse.class).getToken();
    }

    private static final class RestClientWrapper{
        static RestClient instance = new RestClient();
    }
}
