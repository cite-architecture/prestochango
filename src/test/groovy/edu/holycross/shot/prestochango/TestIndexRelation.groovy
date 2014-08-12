package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




class TestIndexRelation extends GroovyTestCase {

  File schemaFile = new File("schemas/CiteCollectionInventory.rng")

  // Archive to use:
  File inventoryFile = new File("testdata/indexedInventory.xml")
  File dataDir = new File("testdata")

  @Test void testIndexing() {
    CollectionArchive cc = new CollectionArchive(inventoryFile, schemaFile, dataDir)    

    CiteUrn urn = new CiteUrn("urn:cite:hmt:scholia")
    assert cc.getRdfVerb(urn, "VisualEvidence") == "dse:illustratedBy"
    assert cc.getRdfVerb(urn, "CtsUrn") == "hmt:commentsOn"

    assert shouldFail {
      String noway = cc.getRdfVerb(urn, "FakeProperty")
    }

    assert shouldFail {
      CiteUrn bogus = new CiteUrn("urn:cite:fake:collection")
      String nothappening = cc.getRdfVerb(bogus, "CtsUrn")
    }

  }


}
