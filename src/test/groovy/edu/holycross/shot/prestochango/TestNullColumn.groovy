package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestNullColumn extends GroovyTestCase {

 String schemaFileName = "schemas/CiteCollectionInventory.rng"

 // Ordered colections MUST have a sequence value
 @Test void testMissingSeq() {
   /*
   String testCsvInventory = "testdata/nullseq.xml"
   File csvInv = new File(testCsvInventory)

   String csvDataDir = "testdata/csvs"
   File csvDir = new File(csvDataDir)

   CollectionArchive cc = new CollectionArchive(csvInv, schemaFileName, csvDir)
   File testOut = new File("testdata/testoutput/hmtNullColCsvOut.ttl")


   assert shouldFail {
     cc.ttl(testOut)
   }*/
 }



}
