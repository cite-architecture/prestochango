package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestEmptyLine extends GroovyTestCase {

 String schemaFileName = "schemas/CiteCollectionInventory.rng"


 
 // OK to have input with empty columns or empty lines:
 @Test void testCsv() {
   String testCsvInventory = "testdata/nullcolumns.xml"
   File csvInv = new File(testCsvInventory)

   String csvDataDir = "testdata/csvs"
   File csvDir = new File(csvDataDir)

   CollectionArchive cc = new CollectionArchive(csvInv, schemaFileName, csvDir)


   File testOut = new File("testdata/testoutput/hmtNullColCsvOut.ttl")
   cc.ttl(testOut)
   System.err.println "TTL in ${testOut}"
 }





}
