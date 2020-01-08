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

public class AddQuoteFailTest {
	Properties prop = new Properties();
	String addQuoteAPI = "quote";
	String apiQuote = "Even if we don't have the power to choose where we come from, we can still choose where we go from there.";
	String apiAuthor = "Stephen Chbosky";
	String apiTags = "inspire";
	String requestHeader = "X-TheySaidSo-Api-Secret";
	String addQuoteid;

	@BeforeTest
	public void getData() throws IOException {

		FileInputStream file = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\java\\RestAssured\\RestAssured\\env.properties");
		prop.load(file);
	}

	@Test(priority = 1)
	public void addNewQuote() {
		int status;
		String message;
		RestAssured.baseURI = prop.getProperty("HOST").concat(addQuoteAPI);
		RequestSpecification addQuoteRequestSpecification = RestAssured.given();
		addQuoteRequestSpecification.contentType("application/json");
		addQuoteRequestSpecification.queryParam("quote", apiQuote);
		addQuoteRequestSpecification.queryParam("author", apiAuthor);
		addQuoteRequestSpecification.queryParam("tags", apiTags);
		Response addQuoteResponse = addQuoteRequestSpecification.request(Method.PUT);
        System.out.printf( addQuoteResponse.prettyPrint());
		status = addQuoteResponse.getStatusCode();
		JSONObject jsonResponse = new JSONObject(addQuoteResponse.getBody().asString());
		message = jsonResponse.getJSONObject("error").get("message").toString();
		Assert.assertEquals(status, 401);
		Assert.assertEquals(message, "Unauthorized");


	}
	
	@Test(priority = 2)
	public void addNewQuoteWithFakeKey() {
		int status;
		String message;
		RestAssured.baseURI = prop.getProperty("HOST").concat(addQuoteAPI);
		RequestSpecification addQuoteRequestSpecification = RestAssured.given();
		addQuoteRequestSpecification.contentType("application/json");
		addQuoteRequestSpecification.header(requestHeader, prop.getProperty("FAKE_KEY"));
		addQuoteRequestSpecification.queryParam("quote", apiQuote);
		addQuoteRequestSpecification.queryParam("author", apiAuthor);
		addQuoteRequestSpecification.queryParam("tags", apiTags);
		Response addQuoteResponse = addQuoteRequestSpecification.request(Method.PUT);
        System.out.printf( addQuoteResponse.prettyPrint());
		status = addQuoteResponse.getStatusCode();
		JSONObject jsonResponse = new JSONObject(addQuoteResponse.getBody().asString());
		message = jsonResponse.getJSONObject("error").get("message").toString();
		Assert.assertEquals(status, 401);
		Assert.assertEquals(message, "Unauthorized");


	}

}
