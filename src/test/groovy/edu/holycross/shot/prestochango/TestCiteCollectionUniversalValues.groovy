package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestCiteCollectionUniversalValues extends GroovyTestCase {



 @Test void testUVals() {

   def vocabList = ["recto", "verso"] as Set
   CiteProperty pageSide = new CiteProperty("rv","Recto or verso side",vocabList)

   CiteUrn collUrn = new CiteUrn("urn:cite:testNs:testColl")
   String descr = "Test collection"
   CiteProperty idProp = new CiteProperty("urn",CitePropertyType.CITE_URN,"canonical id")
   CiteProperty labelProp = new CiteProperty("label",CitePropertyType.STRING,"description of object")

   CiteProperty msA = new CiteProperty("ms",CitePropertyType.CITE_URN,"Codex")
   // Set universal value on property before adding to collection:
   msA.setSingleValue("urn:cite:hmt:msA")

   ArrayList collProps = [idProp, labelProp, pageSide, msA]



   String nsAbbr = "testNs"
   String nsFull = "http://www.testNs.org/datans"

   CiteCollection cc = new CiteCollection(collUrn, descr, idProp, labelProp, null, nsAbbr, nsFull, collProps, null)

   println "UVALS: " + cc.findUniversalValues()
 }
}
