package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestSequence extends GroovyTestCase {

  String inventoryName = "testdata/collections.xml"
  File inv = new File(inventoryName)
  String schemaFileName = "schemas/CiteCollectionInventory.rng"
	File dataDir = new File("testdata/csvs")

 // Ordered colections MUST have a sequence value
 @Test void testOrdering() {
  CollectionArchive ccarchive = new CollectionArchive(inv, schemaFileName, dataDir)
  Cite2Urn urn = new Cite2Urn("urn:cite2:hmt:msA.v1:")
	String ttl = ccarchive.oloOrdering(ccarchive.getCollection(urn))

  assert ccarchive.isOrdered(urn)
  assert  ccarchive.getOrderedByProperty(urn).propertyName == "Sequence"
	ttl.eachLine{ l, i ->
		  if (i < 9){
				System.err.println(l)
			}

			if ( i == 0 ){
				assert l.contains("urn:cite2:hmt:msA:1r")
			}
			if ( i == 1 ){
				assert l.contains("urn:cite2:hmt:msA:1v")
			}
			if ( i == 2 ){
				assert l.contains("urn:cite2:hmt:msA:1v")
			}
			if ( i == 3 ){
				assert l.contains("urn:cite2:hmt:msA:1r")
			}
			if ( i == 4 ){
				assert l.contains("urn:cite2:hmt:msA:2r")
			}

			assert l.contains(":cite:") == false
			assert l.contains(":cite2:")
	}

 }


}
