package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
 */
class TestCitePropertyVocabList extends GroovyTestCase {


	// Ordered colections MUST have a sequence value
	@Test void testConstructor1() {

    def vocabList = ["recto", "verso"] as Set
		CiteProperty testProp = new CiteProperty("rv","Recto or verso side",vocabList)
		assert testProp
    assert testProp.propertyType == CitePropertyType.STRING
    assert testProp.getVocabulary() == vocabList

	}


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
  
	}

}
