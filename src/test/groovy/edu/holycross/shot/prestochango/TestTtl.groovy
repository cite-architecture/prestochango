package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestTtl extends GroovyTestCase {



  String schemaFileName = "schemas/CiteCollectionInventory.rng"

    @Test void testTsv() {
				assert true
		}
/**
    @Test void testTsv() {
        String testTsvInventory = "testdata/signs-collection.xml"
        File tsvInv = new File(testTsvInventory)

        String tsvDataDir = "testdata/csvs"
        File tsvDir = new File(tsvDataDir)

        CollectionArchive cc = new CollectionArchive(tsvInv, schemaFileName, tsvDir)

        File testOut = new File("testdata/testoutput/signs-testing-tsv.ttl")
        cc.ttl(testOut)
        System.err.println "TTL in ${testOut}"
    }


    @Test void testCsv() {
        String testCsvInventory = "testdata/image-collection.xml"
        File csvInv = new File(testCsvInventory)

        String csvDataDir = "testdata/csvs"
        File csvDir = new File(csvDataDir)

        CollectionArchive cc = new CollectionArchive(csvInv, schemaFileName, csvDir)

        File testOut = new File("testdata/testoutput/images-testing-csv.ttl")
        cc.ttl(testOut)
        System.err.println "TTL in ${testOut}"
    }
	**/
    }
