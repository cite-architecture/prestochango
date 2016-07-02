package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestOneItemCollection extends GroovyTestCase {

  String schemaFileName = "schemas/CiteCollectionInventory.rng"
  String response = ""

  @Test void testJustOne() {

    String inventory = "testdata/just-one.xml"
    File invFile = new File(inventory)

    String data = "testdata/csvs"
    File dataDir = new File(data)

    CollectionArchive cc = new CollectionArchive(invFile, schemaFileName, dataDir)

    File testOut = new File("testdata/testoutput/justOne.ttl")

		if (testOut.exists()) {
		  testOut.setText("")
		}

    cc.ttl(testOut)


  }


}
