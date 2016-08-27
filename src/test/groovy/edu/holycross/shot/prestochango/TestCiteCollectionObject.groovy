package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestCiteCollectionObject extends GroovyTestCase {

	/* Make a collection */

	CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
	String descr = "Test collection"
	CiteUrn prevUrn = new CiteUrn("urn:cite:testNs:testColl.one.v1")
	CiteUrn nextUrn = new CiteUrn("urn:cite:testNs:testColl.three.v1")

	CiteProperty idProp = new CiteProperty("urn",CitePropertyType.CITE_URN,"canonical id")
	CiteProperty labelProp = new CiteProperty("label",CitePropertyType.STRING,"description of object")
	CiteProperty orderedByProp = new CiteProperty("seq",CitePropertyType.NUM,"sequence")
	CiteProperty booleanProp = new CiteProperty("trueOrFalse",CitePropertyType.BOOLEAN,"a boolean property")

	ArrayList collProps = [idProp, labelProp, orderedByProp]
	ArrayList extensions = ["cite:CiteImage","cite:Geo"]

	String orderedProp = new CiteProperty("orderedBy",CitePropertyType.NUM,"sequence")
	String nsAbbr = "testNs"
	String nsFull = "http://www.testNs.org/datans"

	CiteCollection ccOrdered = new CiteCollection(collUrn, descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)

	CiteCollection ccUnordered = new CiteCollection(collUrn,descr, idProp, labelProp, null, nsAbbr, nsFull, collProps, extensions)


	@Test void testConstructor1() {

		// Make some property values

		def properties = ["urn":"urn:cite:testNs:testColl.one.v1","label":"object 1","seq":"1"]

		CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.one.v1")

		CiteCollectionObject cco = new CiteCollectionObject(urn,ccUnordered,properties)

		assert cco
	}


	@Test void testConstructor2() {

		def properties = ["urn":"urn:cite:testNs:testColl.one.v1","label":"object 1","seq":"1"]

		CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.two.v1")

		CiteCollectionObject cco = new CiteCollectionObject(urn,ccOrdered,properties,prevUrn,nextUrn)

		assert cco

	}


	@Test void testGetPropertyNames() {



		def properties = ["urn":"urn:cite:testNs:testColl.one.v1","label":"object 1","seq":"1"]

		CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.one.v1")

		CiteCollectionObject cco = new CiteCollectionObject(urn,ccUnordered,properties)

		assert cco.getPropertyNames() == ["urn","label","seq"]

	}


	@Test void testGetPropertyType() {



		def properties = ["urn":"urn:cite:testNs:testColl.one.v1","label":"object 1","seq":"1"]

		CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.one.v1")

		CiteCollectionObject cco = new CiteCollectionObject(urn,ccUnordered,properties)

		assert cco.collection.collProperties.size() == 3
		assert cco.getPropertyType("urn") == "CITE_URN"
		assert cco.getPropertyType("seq") == "NUM"
		assert cco.getPropertyType("label") == "STRING"
	}


	@Test void testGetPropertyValue() {



		def properties = ["urn":"urn:cite:testNs:testColl.one.v1","label":"object 1","seq":"1"]

		CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.one.v1")

		CiteCollectionObject cco = new CiteCollectionObject(urn,ccUnordered,properties)

		assert cco.getPropertyValue("urn").toString() == "urn:cite:testNs:testColl.one.v1"
		assert cco.getPropertyValue("seq") == "1"
		assert cco.getPropertyValue("label") == "object 1"
	}


	@Test void testGetPropertyValue2() {



		def properties = ["urn":"urn:cite:testNs:testColl.two.v1","label":"object 2","seq":"2"]

		CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.two.v1")
		CiteUrn prevUrn = new CiteUrn("urn:cite:testNs:testColl.one.v1")
		CiteUrn nextUrn = new CiteUrn("urn:cite:testNs:testColl.three.v1")

		CiteCollectionObject cco = new CiteCollectionObject(urn,ccOrdered,properties,prevUrn,nextUrn)

		assert cco.urn.toString() == urn.toString()
		assert cco.getPropertyValue("urn").toString() == "urn:cite:testNs:testColl.two.v1"
		assert cco.getPropertyValue("seq") == "2"
		assert cco.getPropertyValue("label") == "object 2"

	}


	@Test void testBadProperties1() {

		ArrayList xcollProps = [idProp, labelProp, orderedByProp,booleanProp]
		ArrayList xextensions = ["cite:CiteImage","cite:Geo"]

		String xorderedProp = "orderedBy"
		String xnsAbbr = "testNs"
		String xnsFull = "http://www.testNs.org/datans"

		CiteCollection ccTest = new CiteCollection(collUrn, descr, idProp, labelProp, orderedByProp, xnsAbbr, xnsFull, xcollProps, xextensions)


		def properties1 = ["urn":"not-a-urn","label":"object 2","seq":"2","trueOrFalse":"true"]
		def properties2 = ["urn":"urn:cite:testNs:testColl.two.v1","label":"object 2","seq":"not-a-number","trueOrFalse":"false"]
		def properties3 = ["urn":"urn:cite:testNs:testColl.two.v1","label":"object 2","seq":"2","trueOrFalse":"notTrueOrFalse"]

		CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.two.v1")

		 shouldFail {
			CiteCollectionObject cco1 = new CiteCollectionObject(urn,ccTest,properties1,prevUrn,nextUrn)
		}

		 shouldFail {
			CiteCollectionObject cco2 = new CiteCollectionObject(urn,ccTest,properties2,prevUrn,nextUrn)
		}

		shouldFail {
			CiteCollectionObject cco3 = new CiteCollectionObject(urn,ccTest,properties3,prevUrn,nextUrn)
		}

	}


	@Test void testPropertyValues2() {

		ArrayList xcollProps = [idProp, labelProp, orderedByProp,booleanProp]
		ArrayList xextensions = ["cite:CiteImage","cite:Geo"]

		String xorderedProp = "orderedBy"
		String xnsAbbr = "testNs"
		String xnsFull = "http://www.testNs.org/datans"

		CiteCollection ccTest = new CiteCollection(collUrn, descr, idProp, labelProp, orderedByProp, xnsAbbr, xnsFull, xcollProps, xextensions)


		def properties1 = ["urn":"urn:cite:testNs:testColl.two.v1","label":"object 2","seq":"2","trueOrFalse":"true"]
		def properties2 = ["urn":"urn:cite:testNs:testColl.two.v1","label":"object 2","seq":"2","trueOrFalse":"false"]

		CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.two.v1")

		CiteCollectionObject cco1 = new CiteCollectionObject(urn,ccTest,properties1,prevUrn,nextUrn)
		assert cco1.getPropertyValue("trueOrFalse") == "true"

		shouldFail{
			assert cco1.getPropertyValue("trueOrFalse") == "false"
		}

		CiteCollectionObject cco2 = new CiteCollectionObject(urn,ccTest,properties2,prevUrn,nextUrn)
		assert cco2.getPropertyValue("trueOrFalse") == "false"
		shouldFail{
			assert cco2.getPropertyValue("trueOrFalse") == true
		}


	}

	@Test void testGetSequence() {



		def properties = ["urn":"urn:cite:testNs:testColl.two.v1","label":"object 2","seq":"2"]

		CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.two.v1")
		CiteUrn prevUrn = new CiteUrn("urn:cite:testNs:testColl.one.v1")
		CiteUrn nextUrn = new CiteUrn("urn:cite:testNs:testColl.three.v1")

		CiteCollectionObject cco = new CiteCollectionObject(urn,ccOrdered,properties,prevUrn,nextUrn)

		assert cco.urn.toString() == urn.toString()
		assert cco.getSequence() == 2

	}

	@Test void testGetSequenceFailsWithUnordered() {



		def properties = ["urn":"urn:cite:testNs:testColl.two.v1","label":"object 2","seq":"2"]

		CiteUrn urn = new CiteUrn("urn:cite:testNs:testColl.two.v1")

		CiteCollectionObject cco = new CiteCollectionObject(urn,ccUnordered,properties)

		assert cco.urn.toString() == urn.toString()
		shouldFail{
			assert cco.getSequence() == 2
		}

	}
}
