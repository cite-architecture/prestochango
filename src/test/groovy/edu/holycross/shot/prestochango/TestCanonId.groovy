package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test



/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestCanonId extends GroovyTestCase {


    String schemaUrl = "http://www.homermultitext.org/hmtschemas/collections/CiteCollectionService.rng"
    CiteUrn coll = new CiteUrn("urn:cite:hmt:msA")
    File tsvInv = new File( "testdata/hmtcollections.xml")
    File tsvDir = new File("testdata/hmtdata")

    @Test void testOrder() {
        CollectionArchive cc = new CollectionArchive(tsvInv, schemaUrl, tsvDir)

        String expectedProp = "URN"

        assert cc.getCanonicalIdProperty(coll) == expectedProp
                               
    }



}