package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestCiteCollectionConstructor extends GroovyTestCase {


 // Ordered colections MUST have a sequence value
 @Test void testConstructor1() {

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")

	CiteProperty idProp = new CiteProperty("urn","citeurn","canonical id")
	CiteProperty labelProp = new CiteProperty("label","string","description of object")
	CiteProperty orderedByProp = new CiteProperty("seq","number","sequence")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = ["cite:CiteImage","cite:Geo"]

	String orderedProp = "orderedBy"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

   String descr = "Test collection"
    CiteCollection cc = new CiteCollection(collUrn, descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

	assert cc
	assert cc.isValid()
/*
    CiteCollection(
		CiteUrn urn,
		String canonicalIdProp,
		String labelProp,
		String orderedByProp,
		String nsAbbr,
		String nsFull,
		ArrayList collProperties ,
		ArrayList extendedBy)
		*/
 }

 // Ordered colections MUST have a sequence value type of "number"
 @Test void testConstructor_badOrderProp() {

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
  String descr = "Test collection"
	CiteProperty idProp = new CiteProperty("urn","citeurn","canonical id")
	CiteProperty labelProp = new CiteProperty("label","string","description of object")
	CiteProperty orderedByProp = new CiteProperty("seq","string","sequence")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = []

	String orderedProp = "orderedBy"
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
	CiteProperty idProp = new CiteProperty("urn","citeurn","canonical id")
	CiteProperty labelProp = new CiteProperty("label","string","description of object")

	ArrayList collProps = [idProp, labelProp]

	String orderedProp = "orderedBy"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

    CiteCollection cc = new CiteCollection(collUrn, descr, idProp, labelProp, null, nsAbbr, nsFull, collProps, null)

	assert cc
	assert cc.isValid()
 }

 @Test void testIsOrdered() {

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
  String descr = "Test collection"
	CiteProperty idProp = new CiteProperty("urn","citeurn","canonical id")
	CiteProperty labelProp = new CiteProperty("label","string","description of object")
	CiteProperty orderedByProp = new CiteProperty("seq","number","sequence")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = ["cite:CiteImage","cite:Geo"]

	String orderedProp = "orderedBy"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

    CiteCollection ccOrdered = new CiteCollection(collUrn, descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

	assert ccOrdered.isValid()
	assert ccOrdered.isOrderedCollection == true

    CiteCollection ccUnordered = new CiteCollection(collUrn,descr, idProp, labelProp, null, nsAbbr, nsFull, collProps, extensions)

	assert ccUnordered.isValid()
	assert ccUnordered.isOrderedCollection == false
 }

 @Test void testGetPropNames() {

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
  String descr = "Test collection"
	CiteProperty idProp = new CiteProperty("urn","citeurn","canonical id")
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

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
  String descr = "Test collection"
	CiteProperty idProp = new CiteProperty("urn","citeurn","canonical id")
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

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
  String descr = "Test collection"
	CiteProperty idProp = new CiteProperty("urn","citeurn","canonical id")
	CiteProperty labelProp = new CiteProperty("label","string","description of object")
	CiteProperty orderedByProp = new CiteProperty("seq","number","sequence")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = ["cite:CiteImage","cite:Geo"]

	String orderedProp = "orderedBy"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

    CiteCollection cc = new CiteCollection(collUrn,descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

	assert cc.getPropertyType("urn") == "citeurn"
	assert cc.getPropertyType("label") == "string"
	assert cc.getPropertyType("seq") == "number"

 }
}
