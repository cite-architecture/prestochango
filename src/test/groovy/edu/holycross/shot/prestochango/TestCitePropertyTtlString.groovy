package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
 */
class TestCitePropertyTtlString extends GroovyTestCase {


	@Test void testCiteUrn() {
		CiteProperty testProp = new	 CiteProperty("img",CitePropertyType.CITE_URN,"Default image")
		assert testProp.asRdfString("urn:cite:hmt:vaimg.Image123") == "<urn:cite:hmt:vaimg.Image123>"

		assert shouldFail {
			 String nogo = testProp.asRdfString("urn:citeless:nogood")
		}
		
	}


}
