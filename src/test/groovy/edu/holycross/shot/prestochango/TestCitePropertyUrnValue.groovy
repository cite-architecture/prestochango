package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
 */
class TestCitePropertyUrnValue extends GroovyTestCase {


	// Ordered colections MUST have a sequence value
	@Test void testConstructor1() {

		CiteProperty testProp = new CiteProperty("urn",CitePropertyType.CITE_URN,"canonical id")
		assert testProp
		assert testProp.propertyName == "urn"
		assert testProp.propertyType == CitePropertyType.CITE_URN
		assert testProp.label == "canonical id"


	}


	@Test void testConstructor2() {
		RdfVerb rdf = new RdfVerb("http://www.homermultitext.org/cite/rdf/citeimage", "citeimg")
		CiteProperty testProp = new CiteProperty("img",CitePropertyType.CITE_URN,"Default image",rdf)
		assert testProp
		assert testProp.propertyName == "img"
		assert testProp.propertyType == CitePropertyType.CITE_URN
		assert testProp.label == "Default image"
    assert testProp.rdf.uri == "http://www.homermultitext.org/cite/rdf/citeimage"
	}

}
