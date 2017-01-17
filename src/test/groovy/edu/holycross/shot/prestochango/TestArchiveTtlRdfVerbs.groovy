package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing prestochange CiteCollection class.
*/
class TestArchiveTtlRdfVerbs extends GroovyTestCase {


  String schemaFileName = "schemas/CiteCollectionInventory.rng"


  @Test void testSingle() {
    File baseDir = new File("testdata")
    if (! baseDir.exists()) {
      baseDir.mkdir()
    }

    String inventoryName = "testdata/just-one.xml"
    File inv = new File(inventoryName)
    CollectionArchive cca = new CollectionArchive(inv, schemaFileName, baseDir)

    def srcList = cca.getDataSources()
    LocalFileSource lfs = srcList["urn:cite2:hmt:vaimg.v1:"]
    CiteCollection cc = cca.getCollection(new Cite2Urn("urn:cite2:hmt:vaimg.v1:"))

    def ttl = cca.turtleizeDataArray(lfs.getRecordArray(), cc)
    println "Resulting tt:\n" + ttl
    // check for one entry with verb "cite:madeUp" and
    // one with verb "cite:upMade"


  }


}
