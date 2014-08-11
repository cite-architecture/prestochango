package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestPhorosAll extends GroovyTestCase {
    String schemaUrl = "http://www.homermultitext.org/hmtschemas/collections/CiteCollectionService.rng"
    String inventory = "/Users/nsmith/repos/git/phoros/collections/inventory.xml"
    String collDir = "/Users/nsmith/repos/git/phoros/collections"

    @Test void testTsv() {
        File inv = new File(inventory)
        File csvDir = new File(collDir)

        CollectionArchive cc = new CollectionArchive(inv, schemaUrl, csvDir)

        File testOut = new File("testdata/testoutput/phorosOut.ttl")
        cc.ttl(testOut)
        System.err.println "TTL in ${testOut}"
    }


    @Test void testCsv() {
        String testCsvInventory = "testdata/states-caps.xml"
        File csvInv = new File(testCsvInventory)

        String csvDataDir = "testdata"
        File csvDir = new File(csvDataDir)

        CollectionArchive cc = new CollectionArchive(csvInv, schemaUrl, csvDir)

        File testOut = new File("testdata/testoutput/csvOut.ttl")
        cc.ttl(testOut)
        System.err.println "TTL in ${testOut}"
    }

}
