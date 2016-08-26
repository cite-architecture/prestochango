package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing prestochange CiteCollection class.
*/
class TestArchiveTtlCollection extends GroovyTestCase {


  String schemaFileName = "schemas/CiteCollectionInventory.rng"


  @Test void testSingle() {
    File baseDir = new File("testdata")
    if (! baseDir.exists()) {
      baseDir.mkdir()
    }

    String inventoryName = "testdata/just-one.xml"
    File inv = new File(inventoryName)
    CollectionArchive cca = new CollectionArchive(inv, schemaFileName, baseDir)

    CiteCollection cc = cca.getCollection(new CiteUrn("urn:cite:hmt:vaimg"))

    def ttl = cca.turtleizeCollection(cc)
    println "Resulting tt:\n" + ttl
    // check for one entry with verb "cite:madeUp" and
    // one with verb "cite:upMade"


    // Test for NS and NS Abbreviation
    Integer checking = 0
    String testVerb = "cite:abbreviatedBy"
    ttl.eachLine{ l ->
        if ( l.contains( testVerb )) {
          System.err.println(l)
          checking++
        }
    }
    assert checking >= 1

    Integer checkingExtension = 0
    testVerb = "cite:extendedBy"
    ttl.eachLine{ l ->
        if ( l.contains( testVerb )) {
          System.err.println(l)
          checkingExtension++
        }
    }
    assert checkingExtension > 0

    Integer checkingPropLabel = 0
    testVerb = "cite:propLabel"
    ttl.eachLine{ l ->
        if ( l.contains( testVerb )) {
          System.err.println(l)
          checkingPropLabel++
        }
    }
    assert checkingPropLabel > 0

  }


}
