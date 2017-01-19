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


}
