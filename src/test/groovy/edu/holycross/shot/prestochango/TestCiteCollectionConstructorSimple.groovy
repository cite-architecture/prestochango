package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestCiteCollectionConstructorSimple extends GroovyTestCase {



 // Handing in nulls for orderedBy and extensions
 @Test void testConstructorNulls() {

	Cite2Urn collUrn = new Cite2Urn("urn:cite2:testNs:testColl.v1:")
  String descr = "Test collection"
	CiteProperty idProp = new CiteProperty("urn",CitePropertyType.CITE2_URN,"canonical id")
	CiteProperty labelProp = new CiteProperty("label",CitePropertyType.STRING,"description of object")

	ArrayList collProps = [idProp, labelProp]

	String orderedProp = "orderedBy"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

    CiteCollection cc = new CiteCollection(collUrn, descr, idProp, labelProp, null, nsAbbr, nsFull, collProps, null)

	assert cc
	assert cc.isValid()
 }
/*
 @Test void testGetPropNames() {

	Cite2Urn collUrn = new Cite2Urn("urn:cite:testNs:testColl")
  String descr = "Test collection"
	CiteProperty idProp = new CiteProperty("urn","Cite2Urn","canonical id")
	CiteProperty labelProp = new CiteProperty("label","string","description of object")
	CiteProperty orderedByProp = new CiteProperty("seq","number","sequence")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = ["cite:CiteImage","cite:Geo"]

	String orderedProp = "orderedBy"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

    CiteCollection cc = new CiteCollection(collUrn, descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

	assert cc.isValid()
	assert cc.getPropertyNames()[0] == "urn"
	assert cc.getPropertyNames()[1] == "label"
	assert cc.getPropertyNames()[2] == "seq"

 }
 @Test void testAboutProperties() {

	Cite2Urn collUrn = new Cite2Urn("urn:cite:testNs:testColl")
  String descr = "Test collection"
	CiteProperty idProp = new CiteProperty("urn","Cite2Urn","canonical id")
	CiteProperty labelProp = new CiteProperty("label","string","description of object")
	CiteProperty orderedByProp = new CiteProperty("seq","number","sequence")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = ["cite:CiteImage","cite:Geo"]

	String orderedProp = "orderedBy"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

    CiteCollection cc = new CiteCollection(collUrn, descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

	assert cc.isValid()
	assert cc.getPropertyNames()[0] == "urn"
	assert cc.collProperties.size() == 3
	assert cc.collProperties[0].propertyName == "urn"


 }

 @Test void testMoreAboutProperties() {

	Cite2Urn collUrn = new Cite2Urn("urn:cite:testNs:testColl")
  String descr = "Test collection"
	CiteProperty idProp = new CiteProperty("urn","Cite2Urn","canonical id")
	CiteProperty labelProp = new CiteProperty("label","string","description of object")
	CiteProperty orderedByProp = new CiteProperty("seq","number","sequence")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = ["cite:CiteImage","cite:Geo"]

	String orderedProp = "orderedBy"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

    CiteCollection cc = new CiteCollection(collUrn,descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

	assert cc.getPropertyType("urn") == "Cite2Urn"
	assert cc.getPropertyType("label") == "string"
	assert cc.getPropertyType("seq") == "number"

 }*/
}
