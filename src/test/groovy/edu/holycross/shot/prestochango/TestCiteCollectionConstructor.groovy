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
	 
    CiteCollection cc = new CiteCollection(collUrn, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

	assert cc
/*
    CiteCollection(
		CiteUrn urn, 
		String canonicalIdProp, 
		String labelProp, 
		String orderedByProp, 
		String nsAbbr, 
		String nsFull, 
		ArrayList properties ,
		ArrayList extendedBy)
		*/
 }

 // Ordered colections MUST have a sequence value type of "number"
 @Test void testConstructor_badOrderProp() {

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
	
	CiteProperty idProp = new CiteProperty("urn","citeurn","canonical id")
	CiteProperty labelProp = new CiteProperty("label","string","description of object")
	CiteProperty orderedByProp = new CiteProperty("seq","string","sequence")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = []
	
	String orderedProp = "orderedBy"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"
	
    shouldFail {	
		CiteCollection cc = new CiteCollection(collUrn, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)
		assert cc
	}
 }


 // Handing in nulls for orderedBy and extensions
 @Test void testConstructorNulls() {

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
	
	CiteProperty idProp = new CiteProperty("urn","citeurn","canonical id")
	CiteProperty labelProp = new CiteProperty("label","string","description of object")

	ArrayList collProps = [idProp, labelProp]
	
	String orderedProp = "orderedBy"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"
	 
    CiteCollection cc = new CiteCollection(collUrn, idProp, labelProp, null, nsAbbr, nsFull, collProps, null)

	assert cc
/*
    CiteCollection(
		CiteUrn urn, 
		String canonicalIdProp, 
		String labelProp, 
		String orderedByProp, 
		String nsAbbr, 
		String nsFull, 
		ArrayList properties ,
		ArrayList extendedBy)
		*/
 }

}
