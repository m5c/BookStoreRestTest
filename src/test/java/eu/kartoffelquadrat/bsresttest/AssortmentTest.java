package eu.kartoffelquadrat.bsresttest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

/**
 * All Unit tests for Assortment Resources.
 *
 * @author Maximilian Schiedermeier
 */
public class AssortmentTest
    extends RestTestUtils {

  /**
   * Verify that GET on /isbns returns 200 and expected catalogue. Every resource is covered by
   * exactly one test.
   */
  @Test
  public void testIsbnsGet() throws UnirestException {

    // Try to retrieve catalogue
    HttpResponse<String> catalogue = Unirest.get(getServiceURL("/isbns")).asString();
    verifyOk(catalogue);

    // Verify and return catalogue content
    String body = catalogue.getBody();
    assert body.contains("9780739360385");
    assert body.contains("9780553382563");
    assert body.contains("9781977791122");
    assert body.contains("9780262538473");
  }

  /**
   * Verify that GET on /isbns/{isbn} returns 200 and expected book details
   */
  @Test
  public void testIsbnsIsbnGet() throws UnirestException {

    // Try to retrieve catalogue
    HttpResponse<String> bookDetails =
        Unirest.get(getServiceURL("/isbns/9780739360385")).asString();
    verifyOk(bookDetails);

    // Verify catalogue content
    assert bookDetails.getBody().contains("priceInCents");
    assert bookDetails.getBody().contains("bookAbstract");
  }

  /**
   * Verify that PUT on /isbns/{isbn} returns 200 and allows adding a book to catalogue. Also
   * verifies the new isbn appears in list and subsequently removes it, to leave server is original
   * state.
   */
  @Test
  public void testIsbnsIsbnPut() throws UnirestException {

    // Using a random ISBN to avoid clash on multiple test run.
    String randomIsbn = getRandomIsbn();
    HttpResponse<String> addBookReply = addTestBook(randomIsbn);
    verifyOk(addBookReply);

    // If Read verification enabled: Verify catalogue content (must now contain the new book)
    if (RestTestUtils.isReadVerficationsRequested()) {
      String catalogue = Unirest.get(getServiceURL("/isbns")).asString().getBody();
      assert catalogue.contains(randomIsbn);
    } else {
      System.out.println("READ VERIFICATIONS SKIPPED TO REDUCE TEST CROSS DEPENDENCIES.");
    }
  }
}
