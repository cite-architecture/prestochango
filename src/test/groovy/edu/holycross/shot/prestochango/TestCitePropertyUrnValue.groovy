package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
 */
class TestCitePropertyUrnValue extends GroovyTestCase {


	// Ordered colections MUST have a sequence value
	@Test void testConstructor1() {

		CiteProperty testProp = new CiteProperty("urn",CitePropertyType.CITE2_URN,"canonical id")
		assert testProp
		assert testProp.propertyName == "urn"
		assert testProp.propertyType == CitePropertyType.CITE2_URN
		assert testProp.label == "canonical id"


	}


	@Test void testConstructor2() {
		RdfVerb rdf = new RdfVerb("dse:illustratedBy", "dse:illustrates")
		CiteProperty testProp = new CiteProperty("img",CitePropertyType.CITE2_URN,"Default image",rdf)
		assert testProp
		assert testProp.propertyName == "img"
		assert testProp.propertyType == CitePropertyType.CITE2_URN
		assert testProp.label == "Default image"
    assert testProp.rdfPair.abbr == "dse:illustratedBy"
	}

}
