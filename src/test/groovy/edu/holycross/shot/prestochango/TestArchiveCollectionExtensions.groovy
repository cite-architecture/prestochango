package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestArchiveCollectionExtensions extends GroovyTestCase {

  String schemaFileName = "schemas/CiteCollectionInventory.rng"


 @Test void testSingle() {
   String inventoryName = "testdata/signs-collection.xml"
   File inv = new File(inventoryName)
  CollectionArchive ccarchive = new CollectionArchive(inv, schemaFileName, new File("/dev/null"))
  CiteUrn urn = new CiteUrn("urn:cite:hmt:critsigns")

  assert ccarchive.extensionsMap.size() == 1
  assert ccarchive.extensionsMap.keySet()[0] == "cite:CiteImage"
  assert ccarchive.extensionsMap["cite:CiteImage"] == "http://www.homermultitext.org/cite/rdf/citeimage"

 }

}
