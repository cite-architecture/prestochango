package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test





class TestExtensionTtl extends GroovyTestCase {


  String schemaFileName = "schemas/CiteCollectionInventory.rng"
  // Archive to use:
  File inventoryFile = new File("testdata/image-collection.xml")
  File dataDir = new File("testdata/csvs")
  String schemaFile = new File("schemas/CiteCollectionInventory.rng")


  @Test void testExtTtl() {
    File testOut = new File("testdata/testoutput/imageExtensionOut.ttl")
    CollectionArchive cc = new CollectionArchive(inventoryFile, schemaFile, dataDir)    
    cc.ttl(testOut)

    // Check output for this line:
    // <urn:cite:hmt:vaimg>  cite:collProperty  citedata:vaimg_Image .
    Integer  expectedRecords= 0 
    testOut.eachLine { l ->
      if (l ==~ /.+cite:possesses.+/ ) {
		expectedRecords++
      }
    }
    Integer extensionRecords = 0
    assert expectedRecords == "THIS IS NOT REALLY IMPLEMENTED, SO OF COURSE IT FAILS."
    System.err.println "TTL in ${testOut}"
  }
}
