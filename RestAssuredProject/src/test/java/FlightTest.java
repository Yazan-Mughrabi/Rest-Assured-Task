import org.json.simple.JSONObject;
import org.testng.annotations.*;
import io.restassured.specification.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

import static utils.RestUtils.*;

import java.io.InputStream;


public class FlightTest {
	
	RequestSpecification requestSpecification;
	String baseBath = "/flights/flight";
	String baseUri = "https://ae.almosafer.com/api/v3";

	
	@BeforeClass
	public void requestBuilder() {
		requestSpecification = new RequestSpecBuilder()
				.setBaseUri(baseUri)
				.setBasePath(baseBath)
				.setContentType(ContentType.JSON)
				.addPathParam("fromDate",getDesiredDate(1L))
				.addPathParam("toDate", getDesiredDate(2L))
				.build();
	}

	@Test
	public void getFlight() {
		
		InputStream createBookingJsonSchema = getClass ().getClassLoader ()
			    .getResourceAsStream ("get-flight-schema.json");

		given()
				.spec(requestSpecification)
		.when()
				.get("/search?query=RUH-JED/{fromDate}/{toDate}/Economy/2Adult")
		.then()
				.statusCode(200)
				.body("next.nid", notNullValue())
				.assertThat().body(matchesJsonSchema(createBookingJsonSchema));
	}

	@Test
	public void postFlight() {
		InputStream createBookingJsonSchema = getClass ().getClassLoader ()
			    .getResourceAsStream ("post-flight-schema.json");
		
		// Perform get flight when needed to get its response for the next api cal
		JSONObject response = performGetFlight(requestSpecification);
		
		given()
				.baseUri(baseUri)
				.basePath(baseBath)
				.header("Content-Type","application/json")
				.body(response)
		.when()
				.post("/async-search-result")
		.then()
				.statusCode(200)
				.assertThat().body(matchesJsonSchema(createBookingJsonSchema));
	}

}
