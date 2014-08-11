package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestFileInv extends GroovyTestCase {
   String schemaFileName = "schemas/CiteCollectionInventory.rng"


    @Test void testUpdatedInventory() {
        String testInventoryName = "testdata/wRevisedSchema/hmtcollections.xml"
        File testInv = new File(testInventoryName)

        String dirName = "testdata/wRevisedSchema"
        File dataDir = new File(dirName)

        CollectionArchive cc = new CollectionArchive(testInv, schemaFileName, dataDir)
	cc.debug = 5
        File testOut = new File("testdata/testoutput/hmtOut.ttl")
        cc.ttl(testOut, true)
        System.err.println "TTL in ${testOut}"
    }



}
