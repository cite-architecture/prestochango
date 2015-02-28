package prestochango.inventory;

import  edu.holycross.shot.prestochango.*;

import org.concordion.integration.junit3.ConcordionTestCase;
import java.io.File;

public class InventoryTest extends ConcordionTestCase {


    /** Path in concordion build to Corpus documentation.  Knowing this
     * lets us write markdown docs with relative references to data files
     * in the documentation.*/
    String docPath = "/build/concordion-results/prestochango/corpus/";

    /** Hands back a String parameter so we can save links using concordion's
     * #Href variable for use in later computations. */
    public String setHref(String path) {
	return (path);
    }




}
