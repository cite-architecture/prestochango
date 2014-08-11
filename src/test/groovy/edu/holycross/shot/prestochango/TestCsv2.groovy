package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestCsv2 extends GroovyTestCase {


   String schemaUrl = "http://www.homermultitext.org/hmtschemas/collections/CiteCollectionService.rng"

    @Test void testCsv() {

        String testCsvInventory = "testdata/hmtcommas.xml"
        File csvInv = new File(testCsvInventory)

        String csvDataDir = "testdata/csvs"
        File csvDir = new File(csvDataDir)

        CollectionArchive cc = new CollectionArchive(csvInv, schemaUrl, csvDir)


        File testOut = new File("testdata/testoutput/hmtCsvOut.ttl")
        cc.ttl(testOut)
        System.err.println "TTL in ${testOut}"
    }

}
