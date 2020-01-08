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

public class SearchQuoteWithFakeKeyTest {
	Properties prop = new Properties();
	String addQuoteAPI = "quote/search";
	String requestHeader = "X-TheySaidSo-Api-Secret";
	String addQuoteid;
	int maxLength = 300, minLength = 100;

	@BeforeTest
	public void getData() throws IOException {

		FileInputStream file = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\java\\RestAssured\\RestAssured\\env.properties");
		prop.load(file);
	}

	@Test(priority = 1)
	public void searchForQuote() {
		int status;
		String message;
		RestAssured.baseURI = prop.getProperty("HOST").concat(addQuoteAPI);
		RequestSpecification searchQuoteRequestSpecification = RestAssured.given();
		searchQuoteRequestSpecification.contentType("application/json");
		searchQuoteRequestSpecification.header(requestHeader, prop.getProperty("FAKE_KEY"));
		searchQuoteRequestSpecification.queryParam("minlength", minLength);
		searchQuoteRequestSpecification.queryParam("maxlength", maxLength);
		Response addQuoteResponse = searchQuoteRequestSpecification.request(Method.GET);
		JSONObject jsonResponse = new JSONObject(addQuoteResponse.getBody().asString());
		status = addQuoteResponse.getStatusCode();
		message = jsonResponse.getJSONObject("error").get("message").toString();
		Assert.assertEquals(status, 401);
		Assert.assertEquals(message, "Unauthorized");

	}
}
