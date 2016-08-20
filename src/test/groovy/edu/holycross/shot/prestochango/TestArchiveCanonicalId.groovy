package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

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
  CollectionArchive cc = new CollectionArchive(inv, schemaFileName, new File("/dev/null"))
  CiteUrn urn = new CiteUrn("urn:cite:hmt:critsigns")

  String expectedPropertyName = "URN"
  assert cc.getCanonicalIdProperty(urn).getPropertyName() == expectedPropertyName

 }

}