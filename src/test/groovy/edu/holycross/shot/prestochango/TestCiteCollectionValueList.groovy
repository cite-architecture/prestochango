package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestCiteCollectionValueList extends GroovyTestCase {



 @Test void testGetPropNames() {
   def vocabList = ["recto", "verso"] as Set
   CiteProperty pageSide = new CiteProperty("rv","Recto or verso side",vocabList)

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
  String descr = "Test collection"
	CiteProperty idProp = new CiteProperty("urn",CitePropertyType.CITE_URN,"canonical id")
	CiteProperty labelProp = new CiteProperty("label",CitePropertyType.STRING,"description of object")

	ArrayList collProps = [idProp, labelProp, pageSide]



	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

  CiteCollection cc = new CiteCollection(collUrn, descr, idProp, labelProp, null, nsAbbr, nsFull, collProps, null)

  assert cc.hasVocabList("rv")
  assert cc.getVocabList("rv") == vocabList
 }




}
