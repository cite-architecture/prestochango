package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




class TestImageExt extends GroovyTestCase {


  // Archive to use:
  File inventoryFile = new File("testdata/image-collection.xml")
  File dataDir = new File("testdata/csvs")
  String schemaFile = new File("schemas/CiteCollectionInventory.rng")


  @Test void testExtension() {
    CiteUrn coll = new CiteUrn("urn:cite:hmt:vaimg")
    def expectedExtensions = ["cite:CiteImage"]
    CollectionArchive cc = new CollectionArchive(inventoryFile, schemaFile, dataDir)    
    assert cc.getExtensionList(coll) == expectedExtensions
  }

  @Test void testBogusCollection() {
    CiteUrn coll = new CiteUrn("urn:cite:totally:fake")
    CollectionArchive cc = new CollectionArchive(inventoryFile, schemaFile, dataDir)    
    assert shouldFail {
      def failList = cc.getExtensionList(coll) 
    }
  }

  @Test void testGetURI() {
    CollectionArchive cc = new CollectionArchive(inventoryFile, schemaFile, dataDir)    
    assert cc.getUriForExtension("cite:CiteImage") == "http://www.homermultitext.org/cite/rdf/citeimage"
  }

}
