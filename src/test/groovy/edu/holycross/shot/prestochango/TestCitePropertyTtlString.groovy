package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
 */
class TestCitePropertyTtlString extends GroovyTestCase {


	@Test void testCite2Urn() {
		CiteProperty testProp = new	 CiteProperty("img",CitePropertyType.CITE2_URN,"Default image")
		assert testProp.asRdfString("urn:cite2:hmt:vaimg.v1:Image123") == "<urn:cite2:hmt:vaimg.v1:Image123>"

		assert shouldFail {
			 String nogo = testProp.asRdfString("urn:citeless:nogood")
		}

		String notCite2String = testProp.asRdfString("urn:cite:hmt:vaimg.123.v1")
		System.err.println("string: ${notCite2String}")
		assert notCite2String == "<urn:cite2:hmt:vaimg.v1:123>"

	}

	@Test void testCite2UrnExtension() {
		CiteProperty testProp = new	 CiteProperty("img",CitePropertyType.CITE2_URN,"Default image")
		assert testProp.asRdfString("urn:cite2:hmt:vaimg.v1:Image123@123,13,12,12") == "<urn:cite2:hmt:vaimg.v1:Image123%40123%2C13%2C12%2C12>"

	}

	@Test void testCtsUrnExtension() {
		CiteProperty testProp = new	 CiteProperty("img",CitePropertyType.CTS_URN,"Default image")
		assert testProp.asRdfString("urn:cts:greekLit:tlg0012.tlg001.msA:1.1@μῆνιν[1]") == "<urn:cts:greekLit:tlg0012.tlg001.msA:1.1%40%CE%BC%E1%BF%86%CE%BD%CE%B9%CE%BD%5B1%5D>"

	}

	@Test void testCtsUrnExtension2() {
		CiteProperty testProp = new	 CiteProperty("img",CitePropertyType.CTS_URN,"Default image")
		assert testProp.asRdfString("urn:cts:greekLit:tlg0012.tlg001.msA:1.1@{>}[1]") == "<urn:cts:greekLit:tlg0012.tlg001.msA:1.1%40%7B%3E%7D%5B1%5D>"

	}

}
