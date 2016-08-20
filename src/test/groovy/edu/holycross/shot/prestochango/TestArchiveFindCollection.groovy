package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing prestochange CiteCollection class.
*/
class TestArchiveFindCollection extends GroovyTestCase {

    String schemaFileName = "schemas/CiteCollectionInventory.rng"


    String testInventory = "testdata/collections.xml"
    File inv = new File(testInventory)

    String dataDir = "testdata/csvs"
    File dir = new File(dataDir)


    @Test void testRetrieval() {
        CollectionArchive ccarchive = new CollectionArchive(inv, schemaFileName, dir)

        CiteUrn collUrn = new CiteUrn("urn:cite:hmt:msA")
        CiteCollection cc = ccarchive.getCollection(collUrn)
        
        assert cc.urn.toString() == "urn:cite:hmt:msA"
        assert cc.canonicalIdProp.propertyName == "URN"
        assert cc.labelProp.propertyName == "Label"

    }
}
