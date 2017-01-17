package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestArchiveCollectionRdf extends GroovyTestCase {
  String inventoryName = "testdata/just-one.xml"
  File inv = new File(inventoryName)
  String schemaFileName = "schemas/CiteCollectionInventory.rng"


 @Test void testSingleValue() {
  CollectionArchive ccarchive = new CollectionArchive(inv, schemaFileName, new File("/dev/null"))
  Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:vaimg.v1:")

  RdfVerb actualVerb =  ccarchive.getRdfVerb(urn,"OtherUrn")
  assert actualVerb.abbr == "cite:madeUp"
  assert actualVerb.inverseAbbr == "cite:upMade"






 }

}
