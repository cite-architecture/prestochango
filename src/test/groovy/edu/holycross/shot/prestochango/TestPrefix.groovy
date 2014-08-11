package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestPrefix extends GroovyTestCase {

    String schemaUrl = "http://www.homermultitext.org/hmtschemas/collections/CiteCollectionService.rng"

    @Test void testPrefixing() {

        String testTsvInventory = "testdata/testcapabilities.xml"
        File tsvInv = new File(testTsvInventory)

        String tsvDataDir = "testdata"
        File tsvDir = new File(tsvDataDir)


        CollectionArchive cc = new CollectionArchive(tsvInv, schemaUrl, tsvDir)

        File testOut = new File("testdata/testoutput/defaultNoPrefix.ttl")
        cc.ttl(testOut)
        
        File testOut2 = new File("testdata/testoutput/withPrefix.ttl")
        cc.ttl(testOut2, true)

        System.err.println "TTL in ${testOut} and ${testOut2}"
    }


}
