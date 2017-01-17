package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing prestochange CiteCollection class.
*/
class TestArchiveDataSources extends GroovyTestCase {


  String schemaFileName = "schemas/CiteCollectionInventory.rng"


  @Test void testSingle() {
    String inventoryName = "testdata/signs-collection.xml"
    File inv = new File(inventoryName)
    CollectionArchive cca = new CollectionArchive(inv, schemaFileName, new File("/dev/null"))
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:critsigns.v1:")

    def srcList = cca.getDataSources()

    assert srcList.size() == 1
    srcList.keySet().each { k ->
      def src = srcList[k]
      assert src.implType == CiteDataSourceType.LOCAL_FILE
    }



  }



  @Test void testMultiple() {
    String testInventory = "testdata/collections.xml"
    File inv = new File(testInventory)

    String dataDir = "testdata/csvs"
    File dir = new File(dataDir)
    CollectionArchive cca = new CollectionArchive(inv, schemaFileName, dir)
    def expectedNumber = 3
    assert cca.getDataSources().size() == expectedNumber

/*
    def expectedUrns = ["urn:cite:hmt:vaimg","urn:cite:hmt:critsigns","urn:cite:hmt:msA"]
    def actualUrns = []
    cca.getCollections().each {
      actualUrns.add(it.urn.toString())
    }
    assert actualUrns == expectedUrns
*/
  }
}
