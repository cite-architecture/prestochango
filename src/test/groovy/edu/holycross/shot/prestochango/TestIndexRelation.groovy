package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




class TestIndexRelation extends GroovyTestCase {

  String schemaFileName = "schemas/CiteCollectionInventory.rng"
  String response = ""

  @Test void testIndexedProperty() {
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


	Integer checkForIndexing = 0

// citedata:vaimg_testUV2 rdf:type rdf:Property .
// citedata:vaimg_testUV1 rdf:type rdf:Property .
	// <urn:cite:hmt:vaimg.VA083RN-0084> citedata:vaimg_OtherUrn <urn:cite:hmt:other.2> .

	testOut.eachLine { l ->
		if ( l.contains("cite:madeUp" )) {
				if ( l.contains("urn:cite:hmt:other.1")) { checkForIndexing++ }
		}
		if ( l.contains("cite:upMade" )) {
				if ( l.contains("urn:cite:hmt:other.1")) { checkForIndexing++ }
		}
		if ( l.contains("cite:madeUp" )) {
				if ( l.contains("urn:cite:hmt:other.2")) { checkForIndexing++ }
		}
		if ( l.contains("cite:upMade" )) {
				if ( l.contains("urn:cite:hmt:other.2")) { checkForIndexing++ }
		}
		if ( l.contains("cite:madeUp" )) {
				if ( l.contains("urn:cite:hmt:other.3")) { checkForIndexing++ }
		}
		if ( l.contains("cite:upMade" )) {
				if ( l.contains("urn:cite:hmt:other.3")) { checkForIndexing++ }
		}
	}

	assert checkForIndexing == 6*/
  }



}
