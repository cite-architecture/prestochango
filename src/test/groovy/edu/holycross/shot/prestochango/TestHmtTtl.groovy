package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestHmtTtl extends GroovyTestCase {

  String schemaFileName = "schemas/CiteCollectionInventory.rng"

    @Test void testHmt() {

        String testTsvInventory = "testdata/hmtcollections.xml"
        File tsvInv = new File(testTsvInventory)

        String tsvDataDir = "testdata/hmtdata"
        File tsvDir = new File(tsvDataDir)

        CollectionArchive cc = new CollectionArchive(tsvInv, schemaFileName, tsvDir)

        File testOut = new File("testdata/testoutput/hmtOut.ttl")
        cc.ttl(testOut)
        System.err.println "TTL in ${testOut}"
    }


}
