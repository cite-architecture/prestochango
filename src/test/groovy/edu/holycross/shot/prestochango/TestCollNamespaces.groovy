package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test

/** Class testing prestochange CiteCollection class.
*/
class TestCollNamespaces extends GroovyTestCase {

    String schemaFileName = "schemas/CiteCollectionInventory.rng"

    String testInventory = "testdata/collections.xml"
    File inv = new File(testInventory)

    String dataDir = "testdata/csvs"
    File dir = new File(dataDir)

	  String expectedNs = """<http://www.homermultitext.org/datans> rdf:type cite:DataNs ."""
    String expectedAbbr = """<http://www.homermultitext.org/datans> cite:abbreviatedBy "hmt" ."""

    @Test void testNamespacesInObject() {
        CollectionArchive cc = new CollectionArchive(inv, schemaFileName, dir)

		assert cc.citeConfig.'urn:cite:hmt:vaimg'.nsfull == "http://www.homermultitext.org/datans"
		assert cc.citeConfig.'urn:cite:hmt:vaimg'.nsabbr == "hmt"

    }
    /*
    @Test void testNamespacesInTTL() {
    	File testOut = new File("testdata/testoutput/testingForNamespaces.ttl")
        CollectionArchive cc = new CollectionArchive(inv, schemaFileName, dir)
    if (testOut.exists()) {
      testOut.setText("")
    }
		cc.ttl(testOut)

    // Check output for this line:
    // <urn:cite:hmt:vaimg>  cite:collProperty  citedata:vaimg_Image .
    Boolean foundFullNS = false
    Boolean foundNSAbbr = false

	//<null> rdf:type cite:DataNs .
//<null> cite:abbreviatedBy "hmt" .

    testOut.eachLine { l ->
      if (l ==~ /<http:\/\/www.homermultitext.org\/datans> +rdf:type +cite:DataNs +\./ ) {
		foundFullNS = true
      }
      if (l ==~ /<http:\/\/www.homermultitext.org\/datans> +cite:abbreviatedBy +"hmt" +\./ ) {
		foundNSAbbr = true
      }
	}

  assert foundFullNS
  assert foundNSAbbr
  }*/

}
