package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestArchiveCollectionNsInfo extends GroovyTestCase {
  String inventoryName = "testdata/signs-collection.xml"
  File inv = new File(inventoryName)
  String schemaFileName = "schemas/CiteCollectionInventory.rng"

 // Ordered colections MUST have a sequence value
 @Test void testConstructor() {
  CollectionArchive ccarchive = new CollectionArchive(inv, schemaFileName, new File("/dev/null"))
  Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:critsigns.v1:")


  String expectedFull = "http://www.homermultitext.org/datans"
  assert  expectedFull == ccarchive.getNsFull(urn)
  assert ccarchive.getNsAbbr(urn)  == "hmt"

 }

}
