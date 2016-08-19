package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestPropTypeEnum extends GroovyTestCase {

 // Ordered colections MUST have a sequence value
 @Test void testLabels() {
   def expectedLabels = ["string value","numeric value","boolean value","markdown string","CITE Object URN","CTS URN"]
    ArrayList testList = CitePropertyType.values()  as ArrayList
   testList.eachWithIndex { n, i ->
     assert n.getLabel() == expectedLabels[i]
   }
 }

}
