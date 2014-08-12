package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestJoin extends GroovyTestCase {

  String schemaFileName = "schemas/CiteCollectionInventory.rng"

  @Test void testJoinProperty() {

    String inventory = "testdata/greekLit/greekLit.xml"
    File invFile = new File(inventory)

    String data = "testdata/greekLit"
    File dataDir = new File(data)

    CollectionArchive cc = new CollectionArchive(invFile, schemaFileName, dataDir)

    File testOut = new File("testdata/testoutput/greekLitOut.ttl")
    cc.ttl(testOut)
    System.err.println "TTL in ${testOut}"
  }


}
