package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test





class TestExtensionTtl extends GroovyTestCase {


  String schemaFileName = "schemas/CiteCollectionInventory.rng"
  // Archive to use:
  File inventoryFile = new File("testdata/collections.xml")
  File dataDir = new File("testdata/csvs")
  String schemaFile = new File("schemas/CiteCollectionInventory.rng")


  @Test void testExtensionMapInObject(){
/*
    CollectionArchive cc = new CollectionArchive(inventoryFile, schemaFile, dataDir)
	assert cc.extensionsMap.'cite:CiteImage' == "http://www.homermultitext.org/cite/rdf/citeimage"
	assert cc.extensionsMap.'cite:GeoJSON' == "http://made.up.uri/rdf/geojson"

  }

  @Test void testExtensionLinkedToCollectionInObject(){
    CollectionArchive cc = new CollectionArchive(inventoryFile, schemaFile, dataDir)

	assert cc.citeConfig.'urn:cite:hmt:vaimg'.citeExtensions[0] == "cite:CiteImage"
  }

  @Test void testExtTtl() {
    File testOut = new File("testdata/testoutput/testingForExtensions.ttl")
    if (testOut.exists()) {
      testOut.setText("")
    }
    CollectionArchive cc = new CollectionArchive(inventoryFile, schemaFile, dataDir)
    cc.ttl(testOut)

	Boolean hasExt = false
	Boolean hasExtAbbr = false
	Boolean collHasExt = false
	Boolean collHasExtInverse = false

    testOut.eachLine { l ->
      if (l ==~ /<http:\/\/www.homermultitext.org\/cite\/rdf\/citeimage> +rdf:type +cite:CiteExtension +\./ ) {
		hasExt = true
      }
      if (l ==~ /<http:\/\/www.homermultitext.org\/cite\/rdf\/citeimage> +cite:abbreviatedBy +cite:CiteImage +\./ ) {
		hasExtAbbr = true
      }
      if (l ==~ /<urn:cite:hmt:vaimg> +cite:extendedBy +cite:CiteImage +\. ?/ ) {
		collHasExt = true
      }
      if (l ==~ /cite:CiteImage +cite:extends +<urn:cite:hmt:vaimg> +\. ?/ ) {
		collHasExtInverse = true
      }
    }

	assert collHasExt
	assert collHasExtInverse
	assert hasExt
	assert hasExtAbbr
*/
  }
}
