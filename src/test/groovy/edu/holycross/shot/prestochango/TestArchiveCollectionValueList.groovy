package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestArchiveCollectionValueList extends GroovyTestCase {
  String inventoryName = "testdata/hmtcollections.xml"
  File inv = new File(inventoryName)
  String schemaFileName = "schemas/CiteCollectionInventory.rng"

 // Ordered colections MUST have a sequence value
 @Test void testConstructor() {
  CollectionArchive ccarchive = new CollectionArchive(inv, schemaFileName, new File("/dev/null"))
  Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:e3.v1:")
println "VALS FOR " + ccarchive.getVocabulary(urn, "RV")

 }

}
