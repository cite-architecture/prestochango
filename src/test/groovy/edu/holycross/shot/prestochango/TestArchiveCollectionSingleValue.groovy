package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestArchiveCollectionSingleValue extends GroovyTestCase {
  String inventoryName = "testdata/just-one.xml"
  File inv = new File(inventoryName)
  String schemaFileName = "schemas/CiteCollectionInventory.rng"


 @Test void testSingleValue() {
  CollectionArchive ccarchive = new CollectionArchive(inv, schemaFileName, new File("/dev/null"))
  Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:vaimg.v1:")


  Cite2Urn expectedUrn =  ccarchive.getSingleValue(urn,"testUV1") as Cite2Urn
  assert expectedUrn.toString() == "urn:cite2:hmt:some:thing1"

  def msg = shouldFail {
    def uval = ccarchive.getSingleValue(urn, "Image")
  }
  assert msg == "CiteProperty: single value not defined for property Image"
 }

}
