package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test





class TestExtensionTtl extends GroovyTestCase {


  String schemaFileName = "schemas/CiteCollectionInventory.rng"
  // Archive to use:
  File inventoryFile = new File("testdata/hmtimgs.xml")
  File dataDir = new File("testdata/images")
  String schemaFile = new File("schemas/CiteCollectionInventory.rng")


  @Test void testExtTtl() {
    File testOut = new File("testdata/testoutput/imageExtensionOut.ttl")
    CollectionArchive cc = new CollectionArchive(inventoryFile, schemaFile, dataDir)    
    cc.ttl(testOut)

    // Check output for this line:
    // <urn:cite:hmt:vaimg>  cite:collProperty  citedata:vaimg_Image .
    System.err.println "TTL in ${testOut}"
  }
}
