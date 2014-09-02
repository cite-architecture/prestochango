package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




class TestConfigFiles extends GroovyTestCase {



  String schemaFileName = "schemas/CiteCollectionInventory.rng"
  File dataDir = new File("testdata/persian")
  File testOut = new File("testdata/testoutput/oldpersian.ttl")

  @Test void testBadConfig() {
    String inventoryName = "testdata/persian/misconfigured.xml"
    File inv = new File(inventoryName)
    CollectionArchive cc = new CollectionArchive(inv, schemaFileName, dataDir)
    assert shouldFail {
      cc.ttl(testOut, true)
    }
  }

  @Test void testGoodConfig() {
    String inventoryName = "testdata/persian/opcollections.xml"
    File inv = new File(inventoryName)
    if (testOut.exists()) {
      testOut.setText("")
    }
    CollectionArchive cc = new CollectionArchive(inv, schemaFileName, dataDir)
    cc.ttl(testOut, true)
    // check on size of testOut: grep a phrase like 'cite:possesses'
    

    Integer expectedRecords = 8
    Integer actualRecords = 0
    testOut.eachLine { l ->
      if (l ==~ /.+cite:possesses.+/ ) {
	actualRecords++
      }
    }
    assert actualRecords == expectedRecords
  }



}
