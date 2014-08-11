package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test


/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestUtf8 extends GroovyTestCase {

   String schemaUrl = "http://www.homermultitext.org/hmtschemas/collections/CiteCollectionService.rng"

    @Test void testCsv() {
        String testCsvInventory = "testdata/archiminv.xml"
        File csvInv = new File(testCsvInventory)

        String csvDataDir = "testdata"
        File csvDir = new File(csvDataDir)

        CollectionArchive cc = new CollectionArchive(csvInv, schemaUrl, csvDir)

        File testOut = new File("testdata/testoutput/lacunaeOut.ttl")
        cc.ttl(testOut)
        System.err.println "TTL in ${testOut}"
    }


    @Test void testTsv() {
        String testTsvInventory = "testdata/archiminv2.xml"
        File tsvInv = new File(testTsvInventory)

        String tsvDataDir = "testdata"
        File tsvDir = new File(tsvDataDir)

        CollectionArchive cc = new CollectionArchive(tsvInv, schemaUrl, tsvDir)

        File testOut = new File("testdata/testoutput/lacunaeOut2.ttl")
        cc.ttl(testOut)
        System.err.println "TTL in ${testOut}"
    }


}
