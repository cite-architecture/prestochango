package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing prestochange CiteCollection class.
*/
class TestInv extends GroovyTestCase {

  String schemaUrl = "file://localhost/Users/nsmith/repos/cite-architecture-github/prestochango/testdata/schemas/CiteCollectionInventory.rng"

  String testInventory = "testdata/testcapabilities.xml"
  String dataDir = "testdata/tsvs"



  @Test void testConstructor() {
    File inv = new File(testInventory)
    File dir = new File(dataDir)
    CollectionArchive cc = new CollectionArchive(inv, schemaUrl, dir)
    assert cc
  }
}
