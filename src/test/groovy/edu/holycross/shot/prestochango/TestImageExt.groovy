package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




class TestImageExt extends GroovyTestCase {


  // Archive to use:
  File inventoryFile = new File("testdata/hmtimgs.xml")
  File dataDir = new File("testdata/images")
  String schemaFile = new File("schemas/CiteCollectionInventory.rng")


  @Test void testExtension() {
    CiteUrn coll = new CiteUrn("urn:cite:hmt:vaimg")
    def expectedExtensions = ["image"]
    CollectionArchive cc = new CollectionArchive(inventoryFile, schemaFile, dataDir)    
    assert cc.getExtensionList(coll) == expectedExtensions
  }

  @Test void testBogusCollection() {
    CiteUrn coll = new CiteUrn("urn:cite:totally:fake")
    CollectionArchive cc = new CollectionArchive(inventoryFile, schemaFile, dataDir)    
    assert shouldFail {
      def failList =    cc.getExtensionList(coll) 
    }
  }

  @Test void testRdfVerb() {
    CollectionArchive cc = new CollectionArchive(inventoryFile, schemaFile, dataDir)    
    assert cc.getRdfTypeForExtension("image") == "cite:CiteImage"
  }

}
