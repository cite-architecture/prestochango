package prestochango.inventory;

import  edu.holycross.shot.prestochango.*;

import org.concordion.integration.junit3.ConcordionTestCase;
import java.io.File;

import edu.harvard.chs.cite.CiteUrn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;
import java.util.SortedSet;

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

    CollectionArchive getCollection(String inventory, String schemaFileName, String archive) throws Exception {
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
	    return(new CollectionArchive(inv, buildPath + schemaFileName, archiveDir));
	} catch (Exception e) {
	    throw e;
	}
    }

    
    /** Finds number of Collections in an inventory */
    public Integer shouldFindNumberOfCollections (String inventory, String schemaFileName, String archive) throws Exception {

	try {
	    CollectionArchive cc = getCollection(inventory, schemaFileName, archive);
	    return (cc.configureFromFile().keySet().size());
	    
	} catch (Exception e) {
	    System.err.println ("InventoryTest: failed with exception " + e.toString());
	    throw e;
	}
	
    }


    /** Gets an interable list of property names for a given collection.
     * @param collectionUrn The Collection we're interested in.
     */
    public Iterable<String> shouldFindPropertyList(String inventory, String schemaFileName, String archive, String collectionUrn) {
	SortedSet<String> props = new TreeSet<String>();
	try {
	    CollectionArchive cc = getCollection(inventory, schemaFileName, archive);
	    CiteUrn urn = new CiteUrn(collectionUrn);
	    ArrayList propList = cc.getPropNameList(urn);
	    props.addAll(propList);
	    return props;
	    
	} catch (Exception e) {
	    System.err.println ("InventoryTest.java failed with exception " + e.toString());
	    return null;
	}
    }

    /** Determines if a given collection is ordered.
     * @param collectionUrn The collection we're interested in.
     */
    public boolean isOrdered(String inventory, String schemaFileName, String archive, String collectionUrn) throws Exception {
	try {
	    CollectionArchive cc = getCollection(inventory, schemaFileName, archive);
	    CiteUrn urn = new CiteUrn(collectionUrn);
	    return cc.isOrdered(urn);
	    
	} catch (Exception e) {
	    System.err.println ("InventoryTest.java failed with exception " + e.toString());
	    throw e;
	}
    }
    

    /** Finds URN of first collection in list */
    public String shouldFindOneUrn (String inventory, String schemaFileName, String archive) throws Exception {
	try {
	    CollectionArchive cc = getCollection(inventory, schemaFileName, archive);
	    java.util.LinkedHashMap config = cc.configureFromFile();
	    Object[] urns = config.keySet().toArray();
	    return (urns[0].toString());

	} catch (Exception e) {
	    System.err.println ("InventoryTest: failed with exception " + e.toString());
	    throw e;
	}
    }



    public String getTypeForName (String inventory, String schemaFileName, String archive, String collUrn, String propName) throws Exception {
	String typeName =  "";
	try {
	    CollectionArchive cc = getCollection(inventory, schemaFileName, archive);
	    CiteUrn urn = new CiteUrn(collUrn);

	    // iterate props until name matches, return type.
	    ArrayList propList = cc.getPropNameList(urn);
	    ArrayList typeList =  cc.getPropTypeList(urn);

	    System.err.println ("Proplist is " + propList + " of size " + propList.size());
	    
	    for (int i  = 0; i < propList.size(); i++) {
		System.err.println("Compare " + propList.get(i).toString() + " to " +  propName) ;
		if (propList.get(i).toString().equals(propName)) {
		    System.err.println("\tEQUAL!");
		    typeName = typeList.get(i).toString() ;
		} else {
		    		    System.err.println("\tnot equal!");
		}
	    }

	} catch (Exception e) {
	    System.err.println ("InventoryTest: failed with exception " + e.toString());
	    throw e;
	}
	return typeName;
    }
    


    public String findCanonicalProperty (String inventory, String schemaFileName, String archive, String collUrn) throws Exception {
	try {
	    CollectionArchive cc = getCollection(inventory, schemaFileName, archive);
	    CiteUrn urn = new CiteUrn(collUrn);
	    return cc.getCanonicalIdProperty(urn);

	} catch (Exception e) {
	    System.err.println ("InventoryTest: failed with exception " + e.toString());
	    throw e;
	}
    }



    public String findLabelProperty (String inventory, String schemaFileName, String archive, String collUrn) throws Exception {
	try {
	    CollectionArchive cc = getCollection(inventory, schemaFileName, archive);
	    CiteUrn urn = new CiteUrn(collUrn);
	    return cc.getLabelProperty(urn);

	} catch (Exception e) {
	    System.err.println ("InventoryTest: failed with exception " + e.toString());
	    throw e;
	}
    }



    

    public String isOrderedBy (String inventory, String schemaFileName, String archive, String collUrn) throws Exception {
	try {
	    CollectionArchive cc = getCollection(inventory, schemaFileName, archive);
	    CiteUrn urn = new CiteUrn(collUrn);
	    return cc.getOrderedByProperty(urn);

	} catch (Exception e) {
	    System.err.println ("InventoryTest, isOrderedBy: failed with exception " + e.toString());
	    throw e;
	}
    }




    public Iterable<String> findValueList(String inventory, String schemaFileName, String archive, String collectionUrn, String propName) {
	SortedSet<String> vals = new TreeSet<String>();
	try {
	    CollectionArchive cc = getCollection(inventory, schemaFileName, archive);
	    CiteUrn urn = new CiteUrn(collectionUrn);
	    ArrayList propList = cc.getValueList(urn, propName);
	    vals.addAll(propList);
	    return vals;
	    
	} catch (Exception e) {
	    System.err.println ("InventoryTest.java failed with exception " + e.toString());
	    return null;
	}
    }

}
