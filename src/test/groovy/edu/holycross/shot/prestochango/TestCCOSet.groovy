package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
 */
class TestCCOSet extends GroovyTestCase {

	 /* Make a collection */

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
	

	CiteProperty idProp = new CiteProperty("urn","citeurn","canonical id")
	CiteProperty labelProp = new CiteProperty("label","string","description of object")
	CiteProperty orderedByProp = new CiteProperty("seq","number","sequence")
	CiteProperty booleanProp = new CiteProperty("trueOrFalse","boolean","a boolean property")

	ArrayList collProps = [idProp, labelProp, orderedByProp,booleanProp]
	ArrayList extensions = ["cite:CiteImage","cite:Geo"]
	
	String orderedProp = "orderedBy"
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"
	 
    CiteCollection ccOrdered = new CiteCollection(collUrn, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)
    CiteCollection ccUnordered = new CiteCollection(collUrn, idProp, labelProp, null, nsAbbr, nsFull, collProps, extensions)

	/* Make some objects */

	CiteCollectionObject cco1 = new CiteCollectionObject(
		new CiteUrn("urn:cite:testNs:testColl.one.v1"),
		ccOrdered,
		["urn":"urn:cite:testNs:testColl.one.v1","label":"object 1","seq":"1"],
		null,
		new CiteUrn("urn:cite:testNs:testColl.two.v1"))

	CiteCollectionObject cco2 = new CiteCollectionObject(
		new CiteUrn("urn:cite:testNs:testColl.two.v1"),
		ccOrdered,
		["urn":"urn:cite:testNs:testColl.two.v1","label":"object 2","seq":"2"],
		new CiteUrn("urn:cite:testNs:testColl.one.v1"),
		new CiteUrn("urn:cite:testNs:testColl.three.v1"))

	CiteCollectionObject cco3 = new CiteCollectionObject(
		new CiteUrn("urn:cite:testNs:testColl.three.v1"),
		ccOrdered,
		["urn":"urn:cite:testNs:testColl.three.v1","label":"object 3","seq":"3"],
		new CiteUrn("urn:cite:testNs:testColl.one.v1"),
		null)

	CiteCollectionObject cco2notUniqueUrn = new CiteCollectionObject(
		new CiteUrn("urn:cite:testNs:testColl.one.v1"),
		ccOrdered,
		["urn":"urn:cite:testNs:testColl.one.v1","label":"object 2","seq":"2"],
		new CiteUrn("urn:cite:testNs:testColl.one.v1"),
		new CiteUrn("urn:cite:testNs:testColl.three.v1"))

	CiteCollectionObject cco2noteUniqueSeq = new CiteCollectionObject(
		new CiteUrn("urn:cite:testNs:testColl.two.v1"),
		ccOrdered,
		["urn":"urn:cite:testNs:testColl.two.v1","label":"object 2","seq":"1"],
		new CiteUrn("urn:cite:testNs:testColl.one.v1"),
		new CiteUrn("urn:cite:testNs:testColl.three.v1"))


 @Test void testConstructor1() {

    CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.one.v1-three.v1")	

	ArrayList ccos = []
	ccos << cco1
	ccos << cco2
	ccos << cco3

	CCOSet ccoset = new CCOSet(ccOrdered,ccos)

	assert ccoset

 }

 @Test void testConstructorShouldFailOnUnordered() {

    CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.one.v1-three.v1")	

	ArrayList ccos = []
	ccos << cco1
	ccos << cco2
	ccos << cco3

	
	shouldFail{
		CCOSet ccoset = new CCOSet(ccUnordered,ccos)
		assert ccoset
	}

 }

 @Test void testConstructorNotUniqueUrns() {

    CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.one.v1-three.v1")	

	ArrayList ccos = []
	ccos << cco1
	ccos << cco2notUniqueUrn
	ccos << cco3

	shouldFail{
		CCOSet ccoset = new CCOSet(ccOrdered,ccos)
		assert ccoset
	}

 }

 @Test void testConstructorNotUniqueSeq() {

    CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.one.v1-three.v1")	

	ArrayList ccos = []
	ccos << cco1
	ccos << cco2noteUniqueSeq
	ccos << cco3

	shouldFail{
		CCOSet ccoset = new CCOSet(ccOrdered,ccos)
		assert ccoset
	}

 }



}

