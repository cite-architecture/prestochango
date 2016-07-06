package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




class TestVersionInTtl extends GroovyTestCase {

  String schemaFileName = "schemas/CiteCollectionInventory.rng"
  String response = ""

  @Test void testForVersion() {

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
	Integer checkForProperFormatting = 0

	testOut.eachLine { l ->
		if ( l.contains("cite:hasVersion" )) {
				if ( l.contains(".v1")) { checkForIndexing++ }
		}
		if ( l.contains("cite:isVersionOf" )) {
				if ( l.contains("v1> cite:")) { checkForIndexing++ }
		}

	}
	
	assert checkForIndexing == 6
  }



}
