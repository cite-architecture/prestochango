package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing prestochange CiteCollection class.
*/
class TestArchiveMetadata extends GroovyTestCase {


  String schemaFileName = "schemas/CiteCollectionInventory.rng"


  @Test void testSingle() {
    String inventoryName = "testdata/signs-collection.xml"
    File inv = new File(inventoryName)
    CollectionArchive cca = new CollectionArchive(inv, schemaFileName, new File("/dev/null"))


    def dcmeta = cca.getDcMeta()


    def signsMeta = dcmeta["urn:cite2:hmt:critsigns.v1:"]
    assert signsMeta["title"] == "Classes of Aristarchan critical signs"

  }



  @Test void testMultiple() {
    String testInventory = "testdata/collections.xml"
    File inv = new File(testInventory)

    String dataDir = "testdata/csvs"
    File dir = new File(dataDir)
    CollectionArchive cca = new CollectionArchive(inv, schemaFileName, dir)
    def expectedNumber = 3
    assert cca.getDcMeta().size() == expectedNumber


  }
}
