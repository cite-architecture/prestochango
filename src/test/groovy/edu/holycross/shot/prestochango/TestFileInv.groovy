package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestFileInv extends GroovyTestCase {
   String schemaUrl = "schemas/CiteCollectionInventory.rng"


    @Test void testUpdatedInventory() {
        String testTsvInventory = "testdata/hmtcollections.xml"
        File tsvInv = new File(testTsvInventory)

        String tsvDataDir = "testdata/hmtdata"
        File tsvDir = new File(tsvDataDir)

        CollectionArchive cc = new CollectionArchive(tsvInv, schemaUrl, tsvDir)

        File testOut = new File("testdata/testoutput/hmtOut.ttl")
        cc.ttl(testOut)
        System.err.println "TTL in ${testOut}"
    }



}
