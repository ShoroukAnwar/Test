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

public class SearchQuoteTest {
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
		String total, quote, author, id;
		RestAssured.baseURI = prop.getProperty("HOST").concat(addQuoteAPI);
		RequestSpecification searchQuoteRequestSpecification = RestAssured.given();
		searchQuoteRequestSpecification.contentType("application/json");
		searchQuoteRequestSpecification.header(requestHeader, prop.getProperty("SECRET_KEY_1"));
		searchQuoteRequestSpecification.queryParam("minlength", minLength);
		searchQuoteRequestSpecification.queryParam("maxlength", maxLength);
		Response addQuoteResponse = searchQuoteRequestSpecification.request(Method.GET);
		JSONObject jsonResponse = new JSONObject(addQuoteResponse.getBody().asString());
		status = addQuoteResponse.getStatusCode();
		total = jsonResponse.getJSONObject("success").get("total").toString();
		quote = jsonResponse.getJSONObject("contents").get("quote").toString();
		author = jsonResponse.getJSONObject("contents").get("author").toString();
		id = jsonResponse.getJSONObject("contents").get("id").toString();
		
		Assert.assertEquals(status, 200);
		Assert.assertEquals(total, "1");
		Assert.assertTrue(!id.isEmpty());
		Assert.assertTrue(!quote.isEmpty());
		Assert.assertTrue(!author.isEmpty());

	}
}
