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

public class AddQuoteTest {
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
}
