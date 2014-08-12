package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestTtl extends GroovyTestCase {



  String schemaFileName = "schemas/CiteCollectionInventory.rng"

    @Test void testTsv() {

        String testTsvInventory = "testdata/hmtcollections.xml"
        File tsvInv = new File(testTsvInventory)

        String tsvDataDir = "testdata/hmtdata"
        File tsvDir = new File(tsvDataDir)

        CollectionArchive cc = new CollectionArchive(tsvInv, schemaFileName, tsvDir)

        File testOut = new File("testdata/testoutput/hmtOut.ttl")
        cc.ttl(testOut)
        System.err.println "TTL in ${testOut}"
    }


    @Test void testCsv() {
        String testCsvInventory = "testdata/states-caps.xml"
        File csvInv = new File(testCsvInventory)

        String csvDataDir = "testdata"
        File csvDir = new File(csvDataDir)

        CollectionArchive cc = new CollectionArchive(csvInv, schemaFileName, csvDir)

        File testOut = new File("testdata/testoutput/csvOut.ttl")
        cc.ttl(testOut)
        System.err.println "TTL in ${testOut}"
    }

}
