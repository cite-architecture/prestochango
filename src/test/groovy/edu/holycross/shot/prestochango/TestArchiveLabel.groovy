package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test


class TestArchiveLabel extends GroovyTestCase {
  String inventoryName = "testdata/signs-collection.xml"
  File inv = new File(inventoryName)
  String schemaFileName = "schemas/CiteCollectionInventory.rng"

 // Ordered colections MUST have a sequence value
 @Test void testConstructor() {
  CollectionArchive ccarchive = new CollectionArchive(inv, schemaFileName, new File("/dev/null"))
  CiteUrn urn = new CiteUrn("urn:cite:hmt:critsigns")

  String expectedPropertyName = "EnglishName"
  assert ccarchive.getLabelProperty(urn).getPropertyName() == expectedPropertyName

 }

}
