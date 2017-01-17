package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
 */
class TestCitePropertyRdf extends GroovyTestCase {


	@Test void testRdf() {
		RdfVerb rdf = new RdfVerb("dse:illustratedBy","http://www.homermultitext.org/dse/rdf/illustratedBy/illustratedBy")
		CiteProperty testProp = new CiteProperty("img",CitePropertyType.CITE2_URN,"Default image",rdf)
		assert testProp
		assert testProp.propertyName == "img"
		assert testProp.propertyType == CitePropertyType.CITE2_URN
		assert testProp.label == "Default image"
    assert shouldFail {
      def vocabList = testProp.getVocabulary()
    }
    assert testProp.rdfPair.abbr == "dse:illustratedBy"
		assert testProp.rdfPair.full == "http://www.homermultitext.org/dse/rdf/illustratedBy/illustratedBy"
	}



  	@Test void testRdf2() {
  		RdfVerb rdf = new RdfVerb
      (
        "dse:illustratedBy",
        "http://www.homermultitext.org/dse/rdf/illustratedBy/illustratedBy",
        "dse:illustrates",
        "http://www.homermultitext.org/dse/rdf/illustratedBy/illustrates"
        )
  		CiteProperty testProp = new CiteProperty("img",CitePropertyType.CITE2_URN,"Default image",rdf)
  		assert testProp
  		assert testProp.propertyName == "img"
  		assert testProp.propertyType == CitePropertyType.CITE2_URN
  		assert testProp.label == "Default image"
      assert shouldFail {
        def vocabList = testProp.getVocabulary()
      }
      assert testProp.rdfPair.abbr == "dse:illustratedBy"
  		assert testProp.rdfPair.inverseAbbr == "dse:illustrates"
  	}
}
