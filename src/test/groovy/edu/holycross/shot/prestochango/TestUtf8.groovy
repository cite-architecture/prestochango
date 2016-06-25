package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test


/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestUtf8 extends GroovyTestCase {



  String schemaFileName = "schemas/CiteCollectionInventory.rng"

    @Test void testCsv() {

		String utfString = "ἀστερίσκος"
        String testCsvInventory = "testdata/signs-collection.xml"
        File csvInv = new File(testCsvInventory)

        String csvDataDir = "testdata/csvs"
        File csvDir = new File(csvDataDir)

        CollectionArchive cc = new CollectionArchive(csvInv, schemaFileName, csvDir)

        File testOut = new File("testdata/testoutput/signs-utf8.ttl")
        cc.ttl(testOut)
		Boolean matchedUtf = false
		testOut.eachLine { l ->
			  if ( l.contains(utfString) ) {
				matchedUtf = true
			  }
		}
        System.err.println "TTL in ${testOut}"
		assert matchedUtf
    }



}
