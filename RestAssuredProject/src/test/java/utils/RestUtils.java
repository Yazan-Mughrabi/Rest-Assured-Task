package utils;

import static io.restassured.RestAssured.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONObject;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestUtils {
	/*
	 * input @param plusDays of type long ex: 1L, 5L
	 * return Desired Date
	 */
	public static String getDesiredDate(long days) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDateTime.now().plusDays(days).format(formatter);
	}
	
	public static JSONObject performGetFlight(RequestSpecification reqSpec) {
		
		Response response = given().spec(reqSpec)
				.when()
				.get("/search?query=RUH-JED/{fromDate}/{toDate}/Economy/2Adult")
				.then()
				.statusCode(200)
				.extract().response();
		JSONObject responseJSONObject = new JSONObject(response.jsonPath().getJsonObject("$"));
		return responseJSONObject;
		
	}
}
