package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
 */
class TestCitePropertyRdf extends GroovyTestCase {


	@Test void testConstructor2() {
		RdfVerb rdf = new RdfVerb("http://www.homermultitext.org/cite/rdf/citeimage", "citeimg")
		CiteProperty testProp = new CiteProperty("img",CitePropertyType.CITE_URN,"Default image",rdf)
		assert testProp
		assert testProp.propertyName == "img"
		assert testProp.propertyType == CitePropertyType.CITE_URN
		assert testProp.label == "Default image"
    assert shouldFail {
      def vocabList = testProp.getVocabulary()
    }
    assert testProp.rdf.uri == "http://www.homermultitext.org/cite/rdf/citeimage"
		assert testProp.rdf.abbr== "citeimg"
	}

}
