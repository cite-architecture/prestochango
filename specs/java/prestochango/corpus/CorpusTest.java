package prestochango.corpus;

import  edu.holycross.shot.prestochango.*;

import org.concordion.integration.junit3.ConcordionTestCase;
import java.io.File;

public class CorpusTest extends ConcordionTestCase {


    /** Path in concordion build to Corpus documentation.  Knowing this
     * lets us write markdown docs with relative references to data files
     * in the documentation.*/
    String docPath = "/build/concordion-results/prestochango/corpus/";

    /** Hands back a String parameter so we can save links using concordion's
     * #Href variable for use in later computations. */
    public String setHref(String path) {
	return (path);
    }


    public boolean shouldMakeArchive(String inventory, String schemaFileName, String archive) {
	try {
	    String buildPath = new java.io.File( "." ).getCanonicalPath() + docPath; 
	    File inv = new File(buildPath + inventory);
	    if (! inv.exists()) {
		System.err.println("Inventory does not exist! " + buildPath + inventory);
	    }
	    
	    File archiveDir = new File(buildPath + archive);
	    if (! archiveDir.exists()) {
		System.err.println("Archive does not exist! " + archive);
	    }


	    CollectionArchive cc = new CollectionArchive(inv, buildPath + schemaFileName, archiveDir);
	    return true;

	
	} catch (Exception e) {
	    System.err.println ("CorpusTest: unable to make archive: " + e.toString());
	    return false;
	}
    }


}
