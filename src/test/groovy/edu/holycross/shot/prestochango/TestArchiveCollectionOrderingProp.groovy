package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestArchiveCollectionOrderingProp extends GroovyTestCase {
  String inventoryName = "testdata/collections.xml"
  File inv = new File(inventoryName)
  String schemaFileName = "schemas/CiteCollectionInventory.rng"

 // Ordered colections MUST have a sequence value
 @Test void testConstructor() {
  CollectionArchive ccarchive = new CollectionArchive(inv, schemaFileName, new File("/dev/null"))
  CiteUrn urn = new CiteUrn("urn:cite:hmt:msA")

  assert ccarchive.isOrdered(urn)
  assert  ccarchive.getOrderedByProperty(urn).propertyName == "Sequence"
 }

}
