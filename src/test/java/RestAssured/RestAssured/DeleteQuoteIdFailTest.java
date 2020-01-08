package RestAssured.RestAssured;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DeleteQuoteIdFailTest {
	Properties prop = new Properties();
	String addQuoteAPI = "quote";
	String requestHeader = "X-TheySaidSo-Api-Secret";

	@BeforeTest
	public void getData() throws IOException {

		FileInputStream file = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\java\\RestAssured\\RestAssured\\env.properties");
		prop.load(file);
	}

	@Test(priority = 2)
	public void deleteQuote() {
		int status;
		String message;
		RestAssured.baseURI = prop.getProperty("HOST").concat(addQuoteAPI);
		RequestSpecification deleteQuoteRequestSpecification = RestAssured.given();
		deleteQuoteRequestSpecification.contentType("application/json");
		deleteQuoteRequestSpecification.header(requestHeader, prop.getProperty("SECRET_KEY_2"));
		deleteQuoteRequestSpecification.queryParam("id", "");
		Response addQuoteResponse = deleteQuoteRequestSpecification.request(Method.DELETE);
		status = addQuoteResponse.getStatusCode();
		JSONObject jsonResponse = new JSONObject(addQuoteResponse.getBody().asString());
		message = jsonResponse.getJSONObject("error").get("message").toString();
		Assert.assertEquals(status, 404);
		Assert.assertEquals(message, "Not Found: Quote not found");

	}
}
