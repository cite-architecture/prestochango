package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing prestochange CiteCollection class.
*/
class TestArchiveTtlOlo extends GroovyTestCase {


  String schemaFileName = "schemas/CiteCollectionInventory.rng"


  @Test void testSingle() {
    File baseDir = new File("testdata")
    if (! baseDir.exists()) {
      baseDir.mkdir()
    }

    String inventoryName = "testdata/signs-collection.xml"
    File inv = new File(inventoryName)
    CollectionArchive cca = new CollectionArchive(inv, schemaFileName, baseDir)

    CiteCollection cc = cca.getCollection(new CiteUrn("urn:cite:hmt:critsigns"))
    println cca.oloOrdering(cc)
    // check for one entry with verb "cite:madeUp" and
    // one with verb "cite:upMade"
  }


}
