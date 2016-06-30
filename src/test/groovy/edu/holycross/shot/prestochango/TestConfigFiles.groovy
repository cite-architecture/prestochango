package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




class TestConfigFiles extends GroovyTestCase {



  String schemaFileName = "schemas/CiteCollectionInventory.rng"
  File dataDir = new File("testdata/csvs")
  File testOut = new File("testdata/testoutput/signs-testing-config.ttl")

  @Test void testBadConfig() {
    if (testOut.exists()) {
      testOut.setText("")
    }
    String inventoryName = "testdata/signs-collection-misconfigured.xml"
    File inv = new File(inventoryName)
    CollectionArchive cc = new CollectionArchive(inv, schemaFileName, dataDir)
    assert shouldFail {
      cc.ttl(testOut, true)
    }
  }

  @Test void testGoodConfig() {
    String inventoryName = "testdata/signs-collection.xml"
    File inv = new File(inventoryName)
    if (testOut.exists()) {
      testOut.setText("")
    }
    CollectionArchive cc = new CollectionArchive(inv, schemaFileName, dataDir)
    cc.ttl(testOut, true)
    // check on size of testOut: grep a phrase like 'cite:possesses'

    Integer expectedRecords = 6 
    Integer actualRecords = 0
    testOut.eachLine { l ->
      if (l ==~ /.+cite:possesses.+/ ) {
	actualRecords++
      }
    }
    assert actualRecords == expectedRecords
  }



}
