package RestAssured.RestAssured;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GetQuoteTest {
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
	//	System.out.println("Response" + addQuoteResponse.prettyPrint());
		JSONObject jsonResponse = new JSONObject(addQuoteResponse.getBody().asString());
		addQuoteid = jsonResponse.getJSONObject("content").getJSONObject("quote").get("id").toString();
		total = jsonResponse.getJSONObject("success").get("total").toString();
		Assert.assertEquals(status, 200);
		Assert.assertEquals(total, "1");
		Assert.assertTrue(!addQuoteid.isEmpty());

	}

	@Test(priority = 2)
	public void getQuoteById() {
		int status, count =0;
		String total, quote, author, id;
		List<String> category = new ArrayList<String>();
		RestAssured.baseURI = prop.getProperty("HOST").concat(addQuoteAPI);
		RequestSpecification getQuoteRequestSpecification = RestAssured.given();
		getQuoteRequestSpecification.contentType("application/json");
		getQuoteRequestSpecification.header(requestHeader, prop.getProperty("SECRET_KEY_1"));
		getQuoteRequestSpecification.queryParam("id", addQuoteid);

		Response addQuoteResponse = getQuoteRequestSpecification.request(Method.GET);

		JSONObject jsonResponse = new JSONObject(addQuoteResponse.getBody().asString());

		status = addQuoteResponse.getStatusCode();
		total = jsonResponse.getJSONObject("success").get("total").toString();
		quote = jsonResponse.getJSONObject("contents").get("quote").toString();
		author = jsonResponse.getJSONObject("contents").get("author").toString();
		id = jsonResponse.getJSONObject("contents").get("id").toString();
		count = jsonResponse.getJSONObject("contents").getJSONArray("categories").length();
		System.out.println();
		
		for (int i = 0; i < count; i++) {
			category.add(i, jsonResponse.getJSONObject("contents").getJSONArray("categories").get(i).toString());
		}
		Assert.assertEquals(status, 200);
		Assert.assertEquals(total, "1");
		Assert.assertTrue(id.contentEquals(addQuoteid));
		Assert.assertTrue(quote.contentEquals(apiQuote));
		Assert.assertTrue(author.contentEquals(apiAuthor));
		for (int i = 0; i < count; i++) {
		    Assert.assertTrue(category.get(i).contentEquals(apiTags));
		}

	}
}
