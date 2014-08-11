package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestHmtLiveTtl extends GroovyTestCase {
   String schemaUrl = "http://www.homermultitext.org/hmtschemas/collections/CiteCollectionService.rng"


    @Test void testHmt() {

        String testTsvInventory = "/Users/nsmith/repos/hmt-bitbucket/hmtxml/cite/collections/hmtcollections.xml"
        File tsvInv = new File(testTsvInventory)


        String tsvDataDir = "/Users/nsmith/repos/hmt-bitbucket/hmtxml/cite/collections"
        File tsvDir = new File(tsvDataDir)

        CollectionArchive cc = new CollectionArchive(tsvInv, schemaUrl, tsvDir)

        File testOut = new File("testdata/testoutput/hmtLiveOut.ttl")
        cc.ttl(testOut)
        System.err.println "TTL in ${testOut}"
    }


}
