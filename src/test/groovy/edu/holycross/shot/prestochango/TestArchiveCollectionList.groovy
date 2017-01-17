package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing prestochange CiteCollection class.
*/
class TestArchiveCollectionList extends GroovyTestCase {


  String schemaFileName = "schemas/CiteCollectionInventory.rng"


  @Test void testSingle() {
    String inventoryName = "testdata/signs-collection.xml"
    File inv = new File(inventoryName)
    CollectionArchive cca = new CollectionArchive(inv, schemaFileName, new File("/dev/null"))
    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:critsigns.v1:")

    def collList = cca.getCollections()
    assert collList.size() == 1
    assert collList[0].urn.toString() ==   "urn:cite2:hmt:critsigns.v1:"
  }



  @Test void testMultiple() {
    String testInventory = "testdata/collections.xml"
    File inv = new File(testInventory)

    String dataDir = "testdata/csvs"
    File dir = new File(dataDir)
    CollectionArchive cca = new CollectionArchive(inv, schemaFileName, dir)
    def expectedNumber = 3
    assert cca.getCollections().size() == expectedNumber

    def expectedUrns = ["urn:cite2:hmt:vaimg.v1:","urn:cite2:hmt:critsigns.v1:","urn:cite2:hmt:msA.v1:"]
    def actualUrns = []
    cca.getCollections().each {
      actualUrns.add(it.urn.toString())
    }
    assert actualUrns == expectedUrns

  }
}
