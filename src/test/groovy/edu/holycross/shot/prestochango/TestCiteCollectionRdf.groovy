package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestCiteCollectionRdf extends GroovyTestCase {



 @Test void testGetPropNames() {
   def vocabList = ["recto", "verso"] as Set
   CiteProperty pageSide = new CiteProperty("rv","Recto or verso side",vocabList)

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
  String descr = "Test collection"
	CiteProperty idProp = new CiteProperty("urn",CitePropertyType.CITE_URN,"canonical id")
	CiteProperty labelProp = new CiteProperty("label",CitePropertyType.STRING,"description of object")


  RdfVerb rdf = new RdfVerb
  (
      "dse:illustratedBy", "http://www.homermultitext.org/dse/rdf/illustratedBy",
  "dse:illustrates", "http://www.homermultitext.org/dse/rdf/illustrates"
      )
  CiteProperty img = new CiteProperty("img",CitePropertyType.CITE_URN,"Default image",rdf)

	ArrayList collProps = [idProp, labelProp, pageSide, img]



	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

  CiteCollection cc = new CiteCollection(collUrn, descr, idProp, labelProp, null, nsAbbr, nsFull, collProps, null)

  assert cc.getRdf("img").abbr == "dse:illustratedBy"
  assert cc.getRdf("img").inverseAbbr == "dse:illustrates"

 }




}
