package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
 */
class TestCitePropertySingleValue extends GroovyTestCase {




		@Test void testSingleValue() {

			CiteUrn msA = new CiteUrn("urn:cite:hmt:msA")

			CiteProperty testProp = new CiteProperty("ms",CitePropertyType.CITE_URN,"Codex")
			CiteUrn retrieved = testProp.getSingleValue()
			assert retrieved.toString() == msA.toString()
		
		}


}
