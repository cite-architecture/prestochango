package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing prestochange CiteCollection class.
*/
class TestDC extends GroovyTestCase {

  String schemaFileName = "schemas/CiteCollectionInventory.rng"

    String testInventory = "testdata/testcapabilities.xml"
    File inv = new File(testInventory)

    String dataDir = "testdata/tsvs"
    File dir = new File(dataDir)


    @Test void testDCMetadata() {
        CollectionArchive cc = new CollectionArchive(inv, schemaFileName, dir)
        CiteUrn collUrn = new CiteUrn("urn:cite:hmt:u4")
        

    }
}
