package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing prestochange CiteCollection class.
*/
class TestGetCollectionList extends GroovyTestCase {

    String schemaFileName = "schemas/CiteCollectionInventory.rng"


    String testInventory = "testdata/collections.xml"
    File inv = new File(testInventory)

    String dataDir = "testdata/csvs"
    File dir = new File(dataDir)


    @Test void testGetList() {
        CollectionArchive cc = new CollectionArchive(inv, schemaFileName, dir)


        def expectedNumber = 3
        assert cc.getCollectionList().size() == expectedNumber

		def expectedCollections = ["urn:cite:hmt:vaimg","urn:cite:hmt:critsigns","urn:cite:hmt:msA"]
		assert expectedCollections.equals(cc.getCollectionList()) 


    }
}
