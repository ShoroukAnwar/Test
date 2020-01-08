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

public class DeleteQuoteUserFailTest {
	Properties prop = new Properties();
	String addQuoteAPI = "quote";
	String apiQuote = "add quote test to test user failure";
	String apiAuthor = "test";
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
		String total;
		RestAssured.baseURI = prop.getProperty("HOST").concat(addQuoteAPI);
		RequestSpecification addQuoteRequestSpecification = RestAssured.given();
		addQuoteRequestSpecification.contentType("application/json");
		addQuoteRequestSpecification.header(requestHeader, prop.getProperty("SECRET_KEY_1"));
		addQuoteRequestSpecification.queryParam("quote", apiQuote);
		addQuoteRequestSpecification.queryParam("author", apiAuthor);
		addQuoteRequestSpecification.queryParam("tags", apiTags);
		Response addQuoteResponse = addQuoteRequestSpecification.request(Method.PUT);
		status = addQuoteResponse.getStatusCode();
		JSONObject jsonResponse = new JSONObject(addQuoteResponse.getBody().asString());
		addQuoteid = jsonResponse.getJSONObject("content").getJSONObject("quote").get("id").toString();
		total = jsonResponse.getJSONObject("success").get("total").toString();
		Assert.assertEquals(status, 200);
		Assert.assertEquals(total, "1");
		Assert.assertTrue(!addQuoteid.isEmpty());

	}
	

	@Test(priority = 2)
	public void deleteQuote() {
		int status;
		String message;
		RestAssured.baseURI = prop.getProperty("HOST").concat(addQuoteAPI);
		RequestSpecification deleteQuoteRequestSpecification = RestAssured.given();
		deleteQuoteRequestSpecification.contentType("application/json");
		deleteQuoteRequestSpecification.header(requestHeader, prop.getProperty("SECRET_KEY_2"));
		deleteQuoteRequestSpecification.queryParam("id", addQuoteid);
		Response addQuoteResponse = deleteQuoteRequestSpecification.request(Method.DELETE);
		System.out.println(addQuoteResponse.prettyPrint());
		status = addQuoteResponse.getStatusCode();
		JSONObject jsonResponse = new JSONObject(addQuoteResponse.getBody().asString());
		message = jsonResponse.getJSONObject("error").get("message").toString();
		Assert.assertEquals(status, 400);
		Assert.assertEquals(message, "Bad Request: Not an owner for this quote");

	}
}
