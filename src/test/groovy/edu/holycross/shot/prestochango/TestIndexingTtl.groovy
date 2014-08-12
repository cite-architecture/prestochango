package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




class TestIndexingTtl extends GroovyTestCase {

  File schemaFile = new File("schemas/CiteCollectionInventory.rng")

  // Archive to use:
  File inventoryFile = new File("testdata/indexedInventory.xml")
  File dataDir = new File("testdata")

  @Test void testIndexing() {
    CollectionArchive cc = new CollectionArchive(inventoryFile, schemaFile, dataDir)    
    cc.debug = 5
    File outFile = new File("testdata/testoutput/indexedColl.ttl")
    cc.ttl(outFile)
  }



}
