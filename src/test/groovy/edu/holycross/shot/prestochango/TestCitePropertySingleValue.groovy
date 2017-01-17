package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.Cite2Urn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
 */
class TestCitePropertySingleValue extends GroovyTestCase {



		@Test void testSingleValue() {

			Cite2Urn msA = new Cite2Urn("urn:cite2:hmt:msA.v1:")

			CiteProperty testProp = new CiteProperty("ms",CitePropertyType.CITE2_URN,"Codex")
			testProp.setSingleValue("urn:cite2:hmt:msA.v1:")

			Cite2Urn retrieved = testProp.getSingleValue() as Cite2Urn
			assert retrieved.toString() == msA.toString()

			CiteProperty noUniversal = new CiteProperty("RV",CitePropertyType.STRING,"recto or verso" )

			def msg = shouldFail {
				def uval = noUniversal.getSingleValue()
			}
      assert msg == "CiteProperty: single value not defined for property RV"

		}


}
