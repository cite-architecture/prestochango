package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestUniversalValue extends GroovyTestCase {

  String schemaFileName = "schemas/CiteCollectionInventory.rng"
  String response = ""

  @Test void testJoinProperty() {
/*
    String inventory = "testdata/one-for-all.xml"
    File invFile = new File(inventory)

    String data = "testdata/csvs"
    File dataDir = new File(data)

    CollectionArchive cc = new CollectionArchive(invFile, schemaFileName, dataDir)

    File testOut = new File("testdata/testoutput/oneForAll.ttl")

		if (testOut.exists()) {
		  testOut.setText("")
		}

    cc.ttl(testOut)


	Integer checkForUVs = 0

// citedata:vaimg_testUV2 rdf:type rdf:Property .
// citedata:vaimg_testUV1 rdf:type rdf:Property .
	// <urn:cite:hmt:vaimg.VA083RN-0084> citedata:vaimg_OtherUrn <urn:cite:hmt:other.2> .

	testOut.eachLine { l ->
		if ( l.contains("citedata:vaimg_testUV2" )) {
				if ( l.contains("urn:cite:hmt:some.thing2")) { checkForUVs++ }
		}
		if ( l.contains("citedata:vaimg_testUV1" )) {
				if ( l.contains("urn:cite:hmt:some.thing1")) { checkForUVs++ }
		}
	}

	assert checkForUVs == 6*/
  }


}
