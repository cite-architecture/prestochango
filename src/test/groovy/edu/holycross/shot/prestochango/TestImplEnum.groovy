package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestImplEnum extends GroovyTestCase {

 // Ordered colections MUST have a sequence value
 @Test void testLabels() {
   def expectedLabels = ["locally stored delimited text file","SQL database"]
   ArrayList testList = ImplementationType.values()  as ArrayList
   testList.eachWithIndex { n, i ->
     assert n.getLabel() == expectedLabels[i]
   }
 }

}
