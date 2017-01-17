package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing prestochange CiteCollection class.
*/
class TestArchiveLocalFiles extends GroovyTestCase {


  String schemaFileName = "schemas/CiteCollectionInventory.rng"


  @Test void testSingle() {
    File baseDir = new File("testdata")
    if (! baseDir.exists()) {
      baseDir.mkdir()
    }


    String inventoryName = "testdata/signs-collection.xml"
    File inv = new File(inventoryName)
    CollectionArchive cca = new CollectionArchive(inv, schemaFileName, baseDir)

    Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:critsigns.v1:")

    def srcList = cca.getDataSources()

    def expectedHeader = ["URN", "EnglishName", "GreekName", "UnicodeHex", "UnicodeNotes"]
    def expectedRecords = 6
    LocalFileSource lfs = srcList[urn.toString()]
    assert lfs.getRecordArray()[0] == expectedHeader
    assert lfs.getRecordArray().size() - 1 == expectedRecords



  }



  @Test void testMultiple() {
    String testInventory = "testdata/collections.xml"
    File inv = new File(testInventory)

    String dataDir = "testdata/csvs"
    File dir = new File(dataDir)
    CollectionArchive cca = new CollectionArchive(inv, schemaFileName, dir)
    def expectedNumber = 3
    assert cca.getDataSources().size() == expectedNumber
  }
}
