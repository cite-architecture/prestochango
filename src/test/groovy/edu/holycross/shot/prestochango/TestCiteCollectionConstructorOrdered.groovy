package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestCiteCollectionConstructorOrdered extends GroovyTestCase {


 // Ordered colections MUST have a sequence value
 @Test void testConstructor1() {

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")

	CiteProperty idProp = new CiteProperty("urn",CitePropertyType.CITE_URN,"canonical id")
	CiteProperty labelProp = new CiteProperty("label",CitePropertyType.STRING,"description of object")
	CiteProperty orderedByProp = new CiteProperty("seq",CitePropertyType.NUM,"sequence")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = ["cite:CiteImage","cite:Geo"]

	String orderedProp = "orderedBy"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

  String descr = "Test collection"
  CiteCollection cc = new CiteCollection(collUrn, descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

	assert cc
	assert cc.isValid()
  assert cc.isOrdered()
 }

 // Ordered colections MUST have a sequence value type of "number"
 @Test void testConstructor_badOrderProp() {

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
  String descr = "Test collection"
  CiteProperty idProp = new CiteProperty("urn",CitePropertyType.CITE_URN,"canonical id")
  CiteProperty labelProp = new CiteProperty("label",CitePropertyType.STRING,"description of object")
	CiteProperty orderedByProp = new CiteProperty("seq",CitePropertyType.STRING,"sequence")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = []


	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

    shouldFail {
		CiteCollection cc = new CiteCollection(collUrn, descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)
		assert cc
	}
 }


 // Handing in nulls for orderedBy and extensions
 @Test void testConstructorNulls() {

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
  String descr = "Test collection"
  CiteProperty idProp = new CiteProperty("urn",CitePropertyType.CITE_URN,"canonical id")
  CiteProperty labelProp = new CiteProperty("label",CitePropertyType.STRING,"description of object")

	ArrayList collProps = [idProp, labelProp]

	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

    CiteCollection cc = new CiteCollection(collUrn, descr, idProp, labelProp, null, nsAbbr, nsFull, collProps, null)

	assert cc
	assert cc.isValid()
  assert (! cc.isOrdered())
 }


}
