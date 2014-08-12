package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test



/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestOrderProp extends GroovyTestCase {

  String schemaFileName = "schemas/CiteCollectionInventory.rng"


    CiteUrn coll = new CiteUrn("urn:cite:hmt:msA")
    String expectedOrderProp = "Sequence"

    @Test void testOrder() {
        String testTsvInventory = "testdata/hmtcollections.xml"
        File tsvInv = new File(testTsvInventory)

        String tsvDataDir = "testdata/hmtdata"
        File tsvDir = new File(tsvDataDir)

        CollectionArchive cc = new CollectionArchive(tsvInv, schemaFileName, tsvDir)

        assert cc.isOrdered(coll)
        assert  cc.getOrderedByProperty(coll) == expectedOrderProp
    }



}
