package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestSequence extends GroovyTestCase {

  String schemaFileName = "schemas/CiteCollectionInventory.rng"
  String response = ""

  @Test void testSequence() {

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


	def numCorrect = 0

	testOut.eachLine { l ->
		if ( l.contains("urn:cite:hmt:vaimg.VA082RN_0083.v1" )) {
			if (l.contains("olo:item")){
				assert l.contains(" 1 ")
				numCorrect++
			}
		}
		if ( l.contains("urn:cite:hmt:vaimg.VA083RN_0084.v1" )) {
			if (l.contains("olo:item")){
				assert l.contains(" 2 ")
				numCorrect++
			}
		}
		if ( l.contains("urn:cite:hmt:vaimg.VA084RN_0085.v1" )) {
			if (l.contains("olo:item")){
				assert l.contains(" 3 ")
				numCorrect++
			}
		}
	}

	assert numCorrect == 3

	numCorrect = 0

	testOut.eachLine { l ->
		if ( l.contains("urn:cite:hmt:vaimg.VA082RN_0083.v1> olo:next" )) {
			if (l.contains("olo:next")){
				assert l.contains("urn:cite:hmt:vaimg.VA083RN_0084.v1")
				numCorrect++
			}
		}
		if ( l.contains("urn:cite:hmt:vaimg.VA083RN_0084.v1> olo:next")) {
			if (l.contains("olo:next")){
				assert l.contains("urn:cite:hmt:vaimg.VA084RN_0085.v1")
				numCorrect++
			}
		}
		if ( l.contains("urn:cite:hmt:vaimg.VA083RN_0084.v1> olo:previous" )) {
			if (l.contains("olo:previous")){
				assert l.contains("urn:cite:hmt:vaimg.VA082RN_0083.v1")
				numCorrect++
			}
		}

	}
	assert numCorrect == 3
	
  }


}
