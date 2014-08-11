package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test



/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestSrcPair extends GroovyTestCase {
    String schemaUrl = "http://www.homermultitext.org/hmtschemas/collections/CiteCollectionService.rng"
    CiteUrn coll = new CiteUrn("urn:cite:hmt:msA")
    File tsvInv = new File( "testdata/hmtcollections.xml")
    File tsvDir = new File("testdata/hmtdata")

    @Test void testPairing() {
        CollectionArchive cc = new CollectionArchive(tsvInv, schemaUrl, tsvDir)


        assert cc.getSourcePair(coll).size() == 2

        def expectedPair = ["file","venetusA.tsv"]
        assert cc.getSourcePair(coll)  == expectedPair
                               
    }



}
