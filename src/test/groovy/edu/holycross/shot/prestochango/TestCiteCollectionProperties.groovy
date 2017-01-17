package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestCiteCollectionProperties extends GroovyTestCase {



 @Test void testGetPropNames() {

	Cite2Urn collUrn = new Cite2Urn("urn:cite2:testNs:testColl.v1:")
  String descr = "Test collection"
	CiteProperty idProp = new CiteProperty("urn",CitePropertyType.CITE2_URN,"canonical id")
	CiteProperty labelProp = new CiteProperty("label",CitePropertyType.STRING,"description of object")
	CiteProperty orderedByProp = new CiteProperty("seq",CitePropertyType.NUM,"sequence")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = ["cite:CiteImage","cite:Geo"]


	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

  CiteCollection cc = new CiteCollection(collUrn, descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

	assert cc.isValid()
	assert cc.getPropertyNames()[0] == "urn"
	assert cc.getPropertyNames()[1] == "label"
	assert cc.getPropertyNames()[2] == "seq"

 }

 @Test void testAboutProperties() {

	Cite2Urn collUrn = new Cite2Urn("urn:cite2:testNs:testColl.v1:")
  String descr = "Test collection"
	CiteProperty idProp = new CiteProperty("urn",CitePropertyType.CITE2_URN,"canonical id")
	CiteProperty labelProp = new CiteProperty("label",CitePropertyType.STRING,"description of object")
	CiteProperty orderedByProp = new CiteProperty("seq",CitePropertyType.NUM,"sequence")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = ["cite:CiteImage","cite:Geo"]

	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

    CiteCollection cc = new CiteCollection(collUrn, descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

	assert cc.isValid()
	assert cc.getPropertyNames()[0] == "urn"
	assert cc.collProperties.size() == 3
	assert cc.collProperties[0].propertyName == "urn"


 }

 @Test void testMoreAboutProperties() {

	Cite2Urn collUrn = new Cite2Urn("urn:cite2:testNs:testColl.v1:")
  String descr = "Test collection"
  CiteProperty idProp = new CiteProperty("urn",CitePropertyType.CITE2_URN,"canonical id")
  CiteProperty labelProp = new CiteProperty("label",CitePropertyType.STRING,"description of object")
  CiteProperty orderedByProp = new CiteProperty("seq",CitePropertyType.NUM,"sequence")
	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = ["cite:CiteImage","cite:Geo"]

	String orderedProp = "orderedBy"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

    CiteCollection cc = new CiteCollection(collUrn,descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

	assert cc.getPropertyType("urn") == CitePropertyType.CITE2_URN
	assert cc.getPropertyType("label") == CitePropertyType.STRING
	assert cc.getPropertyType("seq") == CitePropertyType.NUM
 }


 @Test void findByName() {

   Cite2Urn collUrn = new Cite2Urn("urn:cite2:testNs:testColl.v1:")
  String descr = "Test collection"
  CiteProperty idProp = new CiteProperty("urn",CitePropertyType.CITE2_URN,"canonical id")
  CiteProperty labelProp = new CiteProperty("label",CitePropertyType.STRING,"description of object")
  CiteProperty orderedByProp = new CiteProperty("seq",CitePropertyType.NUM,"sequence")
 ArrayList collProps = [idProp, labelProp, orderedByProp]
 ArrayList extensions = ["cite:CiteImage","cite:Geo"]

 String orderedProp = "orderedBy"
 String nsAbbr = "testNs"
 String nsFull = "http://www.testNs.org/datans"

  CiteCollection cc = new CiteCollection(collUrn,descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

  def prop =cc.propertyForName("urn")
  assert prop.CitePropertyType == CitePropertyType.CITE2_URN
  assert prop.label == "canonical id"

 }
}
