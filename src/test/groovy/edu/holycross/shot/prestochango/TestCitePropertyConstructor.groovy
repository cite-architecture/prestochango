package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
 */
class TestCitePropertyConstructor extends GroovyTestCase {


	// Ordered colections MUST have a sequence value
	@Test void testConstructor1() {

		CiteProperty testProp = new CiteProperty("urn",CitePropertyType.CITE_URN,"canonical id")
		assert testProp
		assert testProp.propertyName == "urn"
		assert testProp.propertyType == CitePropertyType.CITE_URN
		assert testProp.label == "canonical id"

	}


	@Test void testConstructor2() {
		Set values = ["dogs","cats"]
		CiteProperty testProp = new CiteProperty("urn","pet",values)
		assert testProp
		assert testProp.propertyName == "urn"
		assert testProp.propertyType == CitePropertyType.STRING
		assert testProp.valueSet[0] == "dogs"
		assert testProp.label == "pet"
	}

}
