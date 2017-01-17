package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestArchiveCanonicalId extends GroovyTestCase {
  String inventoryName = "testdata/signs-collection.xml"
  File inv = new File(inventoryName)
  String schemaFileName = "schemas/CiteCollectionInventory.rng"

 // Ordered colections MUST have a sequence value
 @Test void testConstructor() {
  CollectionArchive ccarchive = new CollectionArchive(inv, schemaFileName, new File("/dev/null"))
  CiteUrn c1urn = new CiteUrn("urn:cite:hmt:critsigns")
  Cite2Urn c2urn = new Cite2Urn("urn:cite2:hmt:critsigns.v1:")

  // ???
  String expectedPropertyName = "URN"
  assert ccarchive.getCanonicalIdProperty(c2urn).getPropertyName() == expectedPropertyName

 }

}
